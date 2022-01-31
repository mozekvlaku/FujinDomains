package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.helpers.AccessRightLevelEnum;
import net.vespotok.fujin_domains.directory_service.helpers.AccessRightsHelper;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.objects.ContainerObject;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import net.vespotok.fujin_domains.directory_service.model.objects.OrganizationalUnitObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.json.JSONObject;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.*;

@Entity
public class LDAPDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ldapDomain")
    private List<LDAPObject> domainObjects = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, targetEntity = LDAPDomainName.class)
    @JoinColumn(name = "domainName", referencedColumnName = "id")
    private LDAPDomainName domainName;

    @Column(name = "organizationName")
    private String organizationName;

    @Column(name = "organizationLogoURL")
    private String organizationLogoURL;

    @Transient
    private CredentialProvider credentialProvider;

    @Transient
    private Logging l;

    @Transient
    private EntityManager em;

    @Transient
    private AccessRightsHelper ah;

    public LDAPDomain() {
        this.domainName = new LDAPDomainName("persistence.load", LDAPDomainNameTypeEnum.Win2000Style);
        this.organizationName = "No name domain";
        credentialProvider = new CredentialProvider(this);
        ah = new AccessRightsHelper(this);
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+" from database.");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LDAPDomain(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.organizationName = "No name domain";
        domainObjects = new ArrayList<>();
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        ah = new AccessRightsHelper(this);
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        credentialProvider = new CredentialProvider(this);
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public LDAPDomain(LDAPDomainName domainName, String organizationName, String organizationLogoURL)
    {
        this.domainName = domainName;
        this.organizationName = organizationName;
        this.organizationLogoURL = organizationLogoURL;
        ah = new AccessRightsHelper(this);
        domainObjects = new ArrayList<>();
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        credentialProvider = new CredentialProvider(this);
    }

    public void persistLDAPDomain(LDAPDomainName domainName, String organizationName, String organizationLogoURL)
    {
        this.domainName = domainName;
        this.organizationName = organizationName;
        this.organizationLogoURL = organizationLogoURL;
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        credentialProvider = new CredentialProvider(this);
    }

    public void loadObjectsFromDb(EntityManager em)
    {
        this.setDomainName(domainName);
        this.domainObjects = em.createQuery("SELECT o from LDAPObject o").getResultList();

        for (int i = 0; i < domainObjects.size(); i++)
        {
            domainObjects.get(i).loadAttributesFromDb(em);
        }


    }


    public void addObject(LDAPObject object, LDAPUser ldapUser) throws Exception {
        if(ah.hasRightsToAdd(ldapUser))
        {
            object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.administrator, object));
            object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.addModifyDelete, object));
            domainObjects.add(object);
            l.log("Added object "+object.getDN()+" by user " + ldapUser.getUsername());
            if(object.getClass() == UserObject.class)
            {
                try {
                    memberOf(object, getObjectByDn("cn=Domain Users,cn=Users," + this.getDomainName().toDN()), ldapUser);
                }
                catch (Exception e)
                {
                    l.error("Error while adding an object: " + e + " " +"cn=Domain Users,cn=Users," + this.getDomainName().toDN());

                }
            }
            if(em != null) {
                if (!em.getTransaction().isActive()) {
                    em.getTransaction().begin();
                }
                em.persist(object);
                em.getTransaction().commit();
            }
        }
        else
        {
            l.error("Error while adding an object, no rights!");
            throw new Exception("Fujin Domains Exception: You don't have rights to add objects in this domain.");
        }
    }

    public void removeObject(LDAPObject object, LDAPUser ldapUser) throws Exception {
        if(ah.hasRightsToObject(object, ldapUser, AccessRightLevelEnum.BASIC, LDAPAccessRightEnum.modifyDelete))
        {
            domainObjects.remove(object);
            l.log("Removed object "+object.getDN()+" by user " + ldapUser.getUsername());
            em.getTransaction().begin();
            LDAPObject thisObject = em.find(LDAPObject.class, object.getId());
            em.remove(thisObject);
            em.getTransaction().commit();
        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void changeObject(LDAPObject object, LDAPAttributeEnum attributeToChange, String newValue, LDAPUser ldapUser) throws Exception {
        l.log("Got change request, checking permissions");
        if(ah.hasRightsToObject(object, ldapUser, AccessRightLevelEnum.BASIC, LDAPAccessRightEnum.modifyDelete))
        {
            object.changeAttribute(attributeToChange, newValue);
            l.log("Changed object "+object.getDN()+" by user " + ldapUser.getUsername());
            if(em != null) {
                em.getTransaction().begin();
                LDAPObject thisObject = em.find(LDAPObject.class, object.getId());
                em.persist(thisObject);
                em.getTransaction().commit();
            }

        }
        else
        {
            l.error("No permissions!");
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void changeDomain(String newName, String newLogo, LDAPUser ldapUser) throws Exception {
        if(ah.hasRightsToDomain(ldapUser))
        {
            this.organizationName = newName;
            this.organizationLogoURL = newLogo;
            l.log("Changed domain by user " + ldapUser.getUsername());

            em.getTransaction().begin();
            LDAPDomain thisObject = em.find(LDAPDomain.class, this.getId());
            em.persist(thisObject);
            em.getTransaction().commit();

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this domain.");
        }
    }

    public void memberOf(LDAPObject object, LDAPObject what, LDAPUser ldapUser) throws Exception {
        if(ah.hasRightsToObject(object, ldapUser, AccessRightLevelEnum.BASIC, LDAPAccessRightEnum.addModifyDelete))
        {
            String sid1 = object.getSID();
            String sid2 = what.getSID();
            what.appendAttribute(LDAPAttributeEnum.member, sid1);
            object.appendAttribute(LDAPAttributeEnum.memberOf, sid2);
            l.log("Added object "+object.getDN()+" to membership "+what.dn+" by user " + ldapUser.getUsername());
            if(em != null) {
                if (!em.getTransaction().isActive()) {
                    em.getTransaction().begin();
                }

                LDAPObject thisObject = em.find(LDAPObject.class, object.getId());
                em.persist(thisObject);

                LDAPObject thisWhat = em.find(LDAPObject.class, what.getId());
                em.persist(thisWhat);

                em.getTransaction().commit();
            }

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }
    public void removeMember(LDAPObject object, LDAPObject what, LDAPUser ldapUser) throws Exception {
        if(ah.hasRightsToObject(object, ldapUser, AccessRightLevelEnum.BASIC, LDAPAccessRightEnum.addModifyDelete))
        {
            what.removeAppendedAttribute(LDAPAttributeEnum.member, object.getSID());
            object.removeAppendedAttribute(LDAPAttributeEnum.memberOf, what.getSID());
            l.log("Removed object "+object.getDN()+" from membership "+what.dn+" by user " + ldapUser.getUsername());
            em.getTransaction().begin();
            LDAPObject thisObject = em.find(LDAPObject.class, object.getId());
            em.persist(thisObject);
            LDAPObject thisWhat = em.find(LDAPObject.class, what.getId());
            em.persist(thisWhat);
            em.getTransaction().commit();

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public boolean isPartOfGroup(LDAPObject object, LDAPObject group)
    {
        l.log("Checking user/group "+object.getDN()+" if part of group " + group.getDN());
        if(group.getAttributeValue(LDAPAttributeEnum.member).contains(object.getSID()))
        {
            l.success("User/group "+object.getDN()+" is part of group " + group.getDN());
            return true;
        }
        l.error("User/group "+object.getDN()+" is not part of group " + group.getDN());
        return false;
    }

    public ArrayList<LDAPObject> getAllUsers()
    {
        ArrayList<LDAPObject> objectArrayList = new ArrayList<>();
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.objectClass).getAttributeValueString(), "person"))
            {
                objectArrayList.add(object);
            }
        }
        return objectArrayList;
    }
    public ArrayList<LDAPObject> getObjectsBySIDs(String sids)
    {
        ArrayList<LDAPObject> objectArrayList = new ArrayList<>();
        String[] sidArray = sids.split(",");
        for(LDAPObject object : domainObjects)
        {
            if(Arrays.stream(sidArray).anyMatch(object.getAttribute(LDAPAttributeEnum.objectSid).getAttributeValueString()::equals))
            {
                objectArrayList.add(object);
            }
        }
        return objectArrayList;
    }
    public ArrayList<LDAPObject> getAllGroups()
    {
        ArrayList<LDAPObject> objectArrayList = new ArrayList<>();
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.objectClass).getAttributeValueString(), "group"))
            {
                objectArrayList.add(object);
            }
        }
        return objectArrayList;
    }

    public LDAPObject getObjectByDn(String dn)
    {
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString(), dn))
            {
                return object;
            }
        }
        return null;
    }

    public LDAPObject getObjectByUserPrincipalName(String userPrincipalName)
    {
        l.log("To search "+domainObjects.size()+" entities.");
        l.log("Getting: " + userPrincipalName);

        for(LDAPObject object : domainObjects)
        {

            if(object.getAttribute(LDAPAttributeEnum.userPrincipalName) != null && Objects.equals(object.getAttribute(LDAPAttributeEnum.userPrincipalName).getAttributeValueString(), userPrincipalName))
            {
                l.success("Found: "+object.getDN());
                return object;
            }
        }
        l.error("Not found: " + userPrincipalName);

        return null;
    }

    public void loadDefaultGroups(LDAPSystemAdministrator administrator) throws Exception {
        LDAPObject users = new ContainerObject("Users", this);
        LDAPObject computers = new ContainerObject("Computers", this);
        LDAPObject domainControllersOu = new OrganizationalUnitObject("Domain Controllers", this);
        LDAPObject sharesOu = new OrganizationalUnitObject("Shares", this);
        addObject(users, administrator);
        addObject(computers, administrator);
        addObject(domainControllersOu, administrator);
        addObject(sharesOu, administrator);

        LDAPObject domainControllers = new GroupObject("Domain Controllers","Domain Controllers", this);
        LDAPObject domainGuests = new GroupObject("Domain Guests", "Domain Guests", this);
        LDAPObject schemaAdmins = new GroupObject("Schema Admins", "Schema Admins", this);
        LDAPObject serverAdmins = new GroupObject("Server Admins", "Server Admins", this);
        LDAPObject enterpriseAdmins = new GroupObject("Enterprise Admins", "Server Admins", this);
        LDAPObject domainAdmins = new GroupObject("Domain Admins", "Domain Admins", this);
        LDAPObject domainUsers = new GroupObject("Domain Users", "Domain Users", this);
        LDAPObject domainComputers = new GroupObject("Domain Computers", "Domain Computers", this);

        addObject(domainAdmins, administrator);
        addObject(domainComputers, administrator);
        addObject(domainControllers, administrator);
        addObject(domainGuests, administrator);
        addObject(schemaAdmins, administrator);
        addObject(enterpriseAdmins, administrator);
        addObject(serverAdmins, administrator);
        addObject(domainUsers, administrator);

        domainComputers.addToObject(users,em);
        domainControllers.addToObject(users,em);
        domainAdmins.addToObject(users,em);
        domainGuests.addToObject(users,em);
        serverAdmins.addToObject(users,em);
        enterpriseAdmins.addToObject(users,em);
        schemaAdmins.addToObject(users,em);
        domainUsers.addToObject(users,em);
    }

    public LDAPObject getObjectBySAMName(String samName)
    {
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.samAccountName).getAttributeValueString(), samName))
            {
                return object;
            }
        }
        return null;
    }

    public ArrayList<LDAPObject> searchObjectsByName(String query)
    {
        ArrayList<LDAPObject> returnObjects = new ArrayList<>();
        query = query.toLowerCase();
        for(LDAPObject object : domainObjects)
        {
            LDAPAttribute name = object.getAttribute(LDAPAttributeEnum.name);
            LDAPAttribute sn = object.getAttribute(LDAPAttributeEnum.sn);
            LDAPAttribute cn = object.getAttribute(LDAPAttributeEnum.cn);
            LDAPAttribute commonName = object.getAttribute(LDAPAttributeEnum.commonName);
            if(
                (cn != null && cn.getAttributeValueString().toLowerCase().contains(query)) ||
                (sn != null && sn.getAttributeValueString().toLowerCase().contains(query)) ||
                (name != null && name.getAttributeValueString().toLowerCase().contains(query))||
                (commonName != null && commonName.getAttributeValueString().toLowerCase().contains(query))
            )
            {
                l.log("Searching query: "+ query + " returned " + object.getDN());
                returnObjects.add(object);
            }
        }
        return returnObjects;
    }

    public LDAPDomainName getDomainName()
    {
        return this.domainName;
    }

    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationLogoURL() {
        return organizationLogoURL;
    }

    public List<LDAPObject> getDomainObjects() {
        return domainObjects;
    }

    public void setDomainObjects(List<LDAPObject> domainObjects) {
        this.domainObjects = domainObjects;
    }

    public void setDomainName(LDAPDomainName domainName) {
        this.domainName = domainName;
        l.log("Persistence loaded domain changed its domain name to "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        this.credentialProvider.setLdapDomain(this);

    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setOrganizationLogoURL(String organizationLogoURL) {
        this.organizationLogoURL = organizationLogoURL;
    }

    public void setCredentialProvider(CredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
    }

    public Logging getL() {
        return l;
    }

    public void setL(Logging l) {
        this.l = l;
    }
}
