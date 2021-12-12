package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.objects.ContainerObject;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import net.vespotok.fujin_domains.directory_service.model.objects.OrganizationalUnitObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LDAPDomain {
    private ArrayList<LDAPObject> domainObjects;
    private LDAPDomainName domainName;
    private String organizationName;
    private String organizationLogoURL;

    private CredentialProvider credentialProvider;

    private Logging l;

    public LDAPDomain(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.organizationName = "No name domain";
        domainObjects = new ArrayList<>();
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        credentialProvider = new CredentialProvider(this);
    }

    public LDAPDomain(LDAPDomainName domainName, String organizationName, String organizationLogoURL)
    {
        this.domainName = domainName;
        this.organizationName = organizationName;
        this.organizationLogoURL = organizationLogoURL;
        domainObjects = new ArrayList<>();
        l = new Logging(LoggingLevel.print,domainName, "Domain");
        l.log("Registered domain "+domainName.toWin2000Style() +" ("+domainName.toNT4Style()+")"+".");
        credentialProvider = new CredentialProvider(this);
    }

    public void addObject(LDAPObject object, LDAPUser ldapUser)
    {
        object.setDomainName(this.domainName);
        object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.administrator));
        object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.addModifyDelete));
        domainObjects.add(object);
        l.log("Added object "+object.getDN()+" by user " + ldapUser.getUsername());
    }

    public void removeObject(LDAPObject object, LDAPUser ldapUser) throws Exception {
        if(object.hasRightsToModify(ldapUser))
        {
            domainObjects.remove(object);
            l.log("Removed object "+object.getDN()+" by user " + ldapUser.getUsername());

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void changeObject(LDAPObject object, LDAPAttributeEnum attributeToChange, String newValue, LDAPUser ldapUser) throws Exception {
        if(object.hasRightsToModify(ldapUser))
        {
            object.changeAttribute(attributeToChange, newValue);
            l.log("Changed object "+object.getDN()+" by user " + ldapUser.getUsername());

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void memberOf(LDAPObject object, LDAPObject what, LDAPUser ldapUser) throws Exception {
        if(object.hasRightsToModify(ldapUser))
        {
            object.addAttribute(new LDAPAttribute(LDAPAttributeEnum.memberOf, what.getDN()));
            l.log("Added object "+object.getDN()+" to membership "+what.dn+" by user " + ldapUser.getUsername());

        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
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

    public void loadDefaultGroups(LDAPSystemAdministrator administrator)
    {
        LDAPObject users = new ContainerObject("Users");
        LDAPObject computers = new ContainerObject("Computers");
        LDAPObject domainControllersOu = new OrganizationalUnitObject("Domain Controllers");
        LDAPObject sharesOu = new OrganizationalUnitObject("Shares");
        addObject(users, administrator);
        addObject(computers, administrator);
        addObject(domainControllersOu, administrator);
        addObject(sharesOu, administrator);

        LDAPObject domainControllers = new GroupObject("Domain Controllers","Domain Controllers");
        LDAPObject domainGuests = new GroupObject("Domain Guests", "Domain Guests");
        LDAPObject schemaAdmins = new GroupObject("Schema Admins", "Schema Admins");
        LDAPObject domainAdmins = new GroupObject("Domain Admins", "Domain Admins");
        LDAPObject domainComputers = new GroupObject("Domain Computers", "Domain Computers");

        addObject(domainAdmins, administrator);
        addObject(domainComputers, administrator);
        addObject(domainControllers, administrator);
        addObject(domainGuests, administrator);
        addObject(schemaAdmins, administrator);

        domainComputers.addToObject(users);
        domainControllers.addToObject(users);
        domainAdmins.addToObject(users);
        domainGuests.addToObject(users);
        schemaAdmins.addToObject(users);
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
}
