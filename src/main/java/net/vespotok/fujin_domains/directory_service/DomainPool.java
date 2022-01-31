package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;

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

    public DomainPool()
    {
        domainList = new ArrayList<>();
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Domain Pool");
        l.log("Domain pool created.");


    }

    public void setDs(DirectoryServer ds)
    {
        this.ds = ds;
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

    }

    public void loadDomainsFromDb() throws Exception {
        l.log("Loading domains from database.");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fjdcs-server");
        EntityManager em = emf.createEntityManager();
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
