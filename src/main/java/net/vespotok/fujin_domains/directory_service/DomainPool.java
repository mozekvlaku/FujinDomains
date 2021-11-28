package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;

import java.util.ArrayList;
import java.util.Objects;

public class DomainPool {
    private ArrayList<LDAPDomain> domainList;

    public DomainPool()
    {
        domainList = new ArrayList<>();
    }

    public void addDomain(LDAPDomain domain)
    {
        domainList.add(domain);
    }

    public LDAPDomain getDomainByDomainName(LDAPDomainName domainName) throws Exception {
        if(!this.domainList.contains(domainName.toWin2000Style()))
        {
            throw new Exception("This domain was not found.");
        }
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName(), domainName))
            {
                return domain;
            }
        }
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
        for(LDAPDomain domain : this.domainList)
        {
            if(Objects.equals(domain.getDomainName().toWin2000Style(), domainName))
            {
                return domain;
            }
        }
        return null;
    }
}
