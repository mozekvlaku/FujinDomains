package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.credential_provider.DomainDispatcher;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPSystemAdministrator;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import org.springframework.stereotype.Component;

public class DirectoryServer {
    public DomainPool domainPool;
    public DomainDispatcher domainDispatcher;
    private String administratorPassword;
    private String serverAddress;
    private Logging l;

    public DirectoryServer(String serverAddress, String administratorPassword)
    {
        this.serverAddress = serverAddress;
        this.administratorPassword = administratorPassword;
        this.domainPool = new DomainPool();
        this.domainDispatcher = new DomainDispatcher(this);
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Server");
        l.log("Started a Fujin Domain Services server running on " + serverAddress + ".");
    }



    public String getServerAddress() {
        return serverAddress;
    }
}
