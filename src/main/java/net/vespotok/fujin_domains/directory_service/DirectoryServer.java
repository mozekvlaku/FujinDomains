package net.vespotok.fujin_domains.directory_service;

import com.fasterxml.classmate.AnnotationConfiguration;
import net.vespotok.fujin_domains.directory_service.credential_provider.DomainDispatcher;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;

import java.net.InetAddress;


public class DirectoryServer {
    public DomainPool domainPool;
    public DomainDispatcher domainDispatcher;
    private String administratorPassword;
    private String serverAddress;
    private Logging l;

    public DirectoryServer() throws Exception {
        this.serverAddress = "";
        this.administratorPassword = "admin1";
        this.domainPool = new DomainPool();
        this.domainDispatcher = new DomainDispatcher(this);
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Server");
        l.log("Started a Fujin Domain Services server running on " + serverAddress + ".");

        this.domainPool.setDs(this);
        this.domainPool.loadDomainsFromDb();
    }

    public DirectoryServer(String serverAddress, String administratorPassword) throws Exception {
        this.serverAddress = serverAddress;
        this.administratorPassword = administratorPassword;
        this.domainPool = new DomainPool();
        this.domainDispatcher = new DomainDispatcher(this);
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Server");
        l.log("Started a Fujin Domain Services server running on " + serverAddress + ".");

        this.domainPool.setDs(this);
        this.domainPool.loadDomainsFromDb();

    }

    public DomainPool getDomainPool() {
        return domainPool;
    }

    public void setDomainPool(DomainPool domainPool) {
        this.domainPool = domainPool;
    }

    public DomainDispatcher getDomainDispatcher() {
        return domainDispatcher;
    }

    public void setDomainDispatcher(DomainDispatcher domainDispatcher) {
        this.domainDispatcher = domainDispatcher;
    }

    public String getAdministratorPassword() {
        return administratorPassword;
    }

    public void setAdministratorPassword(String administratorPassword) {
        this.administratorPassword = administratorPassword;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getServerAddress() {
        return serverAddress;
    }
}
