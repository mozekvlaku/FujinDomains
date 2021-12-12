package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;
import org.apache.juli.logging.Log;

import java.util.ArrayList;
import java.util.Objects;

public class DomainPool {
    private ArrayList<LDAPDomain> domainList;

    private Logging l;

    public DomainPool()
    {
        domainList = new ArrayList<>();
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Domain Pool");
        l.log("Domain pool created.");
    }

    public void addDomain(LDAPDomain domain)
    {
        domainList.add(domain);
        l.log("Added domain " + domain.getDomainName().toWin2000Style() + " to the domain pool.");
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
