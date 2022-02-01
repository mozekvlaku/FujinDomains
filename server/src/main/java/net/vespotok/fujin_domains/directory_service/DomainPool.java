package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.credential_provider.Credential;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class DomainPool {


    private List<LDAPDomain> domainList;

    private Logging l;

    private DirectoryServer ds;
    private EntityManager em;

    public DomainPool(DirectoryServer ds)
    {
        domainList = new ArrayList<>();
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Domain Pool");
        l.log("Domain pool created.");
        this.ds = ds;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fjdcs-server");
        em = emf.createEntityManager();
    }


    public void addDomain(LDAPDomain domain)
    {
        domainList.add(domain);
        l.log("Added domain " + domain.getDomainName().toWin2000Style() + " to the domain pool.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fjdcs-server");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(domain);
        em.getTransaction().commit();
        em.close();
        emf.close();

        domain.setEm(this.em);
        domain.setDs(this.ds);
    }
    public void removeDomain(LDAPDomain domain)
    {
        domainList.remove(domain);
        l.log("Removed domain " + domain.getDomainName().toWin2000Style() + " to the domain pool.");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fjdcs-server");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        LDAPDomain thisDomain = em.find(LDAPDomain.class, domain.getId());
        em.remove(thisDomain);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public void loadDomainsFromDb() throws Exception {
        l.log("Loading domains from database.");


        em.getTransaction().begin();
        this.domainList = em.createQuery("SELECT d from LDAPDomain d").getResultList();
        //this.domainList = em.findAll;

        for (int i = 0; i < domainList.size(); i++)
        {
            l.log("Persist-loading "+domainList.get(i).getDomainName().toWin2000Style()+" with id "+domainList.get(i).getId()+".");
            /*domainList.get(i).loadObjectsFromDb(em);*/
            LDAPDomainName ldapDomainName = domainList.get(i).getDomainName();
            String organizationName = domainList.get(i).getOrganizationName();
            String organizationLogo = domainList.get(i).getOrganizationLogoURL();
            domainList.get(i).persistLDAPDomain(ldapDomainName, organizationName,organizationLogo);
            domainList.get(i).setEm(em);
            domainList.get(i).setDs(ds);
            em.persist(domainList.get(i));

        }



        em.getTransaction().commit();

        if(domainList.size() == 0)
        {
            DefaultGetter df = new DefaultGetter(this.ds);
        }
       /* em.close();
        emf.close();*/

        l.log("Loaded "+this.domainList.size()+" domains from database.");
        l.success("System ready");
    }

    public boolean domainExists(LDAPDomainName domainName)
    {
        l.log("Searching for domain " + domainName.toWin2000Style()+".");
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toWin2000Style(), domainName.toWin2000Style())) {
                l.success("Domain " + domain.getDomainName().toWin2000Style() + " found.");

                return true;
            }
        }
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toNT4Style(), domainName.toNT4Style())) {
                l.success("Domain " + domain.getDomainName().toWin2000Style() + " found.");

                return true;
            }
        }


        l.error("Domain " + domainName.toWin2000Style()+" not found.");

        return false;
    }

    public Credential getDomainCredentialByToken(String token) throws Exception {
        for(LDAPDomain domain : domainList)
        {
            if(domain.getCredentialProvider().getCredential(token) != null)
            {
                return domain.getCredentialProvider().getCredential(token);
            }
        }
        return null;
    }

    public LDAPDomain getDomainByLDAPUser(LDAPUser user)
    {
        l.log("Searching for domain with user " + user.getUsername()+".");

        for(LDAPDomain domain : domainList)
        {
            if(domain.getObjectByDn(user.getDN()) != null)
            {
                l.success("User " + user.getUsername()+" found in domain "+domain.getDomainName().toWin2000Style()+".");

                return domain;
            }
        }
        l.error("User " + user.getUsername()+" was not found anywhere.");

        return null;
    }

    public LDAPDomain getDomainByDomainName(LDAPDomainName domainName) throws Exception {
        l.log("Searching for domain " + domainName.toWin2000Style()+".");

        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toWin2000Style(), domainName.toWin2000Style()))
            {
                l.success("Domain " + domain.getDomainName().toWin2000Style()+" found.");

                return domain;
            }
        }
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toNT4Style(), domainName.toNT4Style()))
            {
                l.success("Domain " + domain.getDomainName().toWin2000Style()+" found.");

                return domain;
            }
        }
        l.error("Domain " + domainName.toWin2000Style()+" not found.");

        throw new Exception("This domain was not found.");
    }

    public String[] getDomainNamesList()
    {
        ArrayList<String> domainNames = new ArrayList<>();
        for(LDAPDomain domain : this.domainList)
        {
            domainNames.add(domain.getDomainName().toWin2000Style());
        }
        return domainNames.toArray(new String[0]);
    }

    public List<LDAPDomain> getDomains()
    {
        return domainList;
    }


    public LDAPDomain getDomainByDomainName(String domainName)
    {
        l.log("Searching for domain " + domainName+".");
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toWin2000Style(), domainName))
            {
                l.success("Domain " + domain.getDomainName().toWin2000Style()+" found.");
                return domain;
            }
        }
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toNT4Style(), domainName))
            {
                l.success("Domain " + domain.getDomainName().toWin2000Style()+" found.");
                return domain;
            }
        }
        l.error("Domain " + domainName+" not found.");

        return null;
    }
}
