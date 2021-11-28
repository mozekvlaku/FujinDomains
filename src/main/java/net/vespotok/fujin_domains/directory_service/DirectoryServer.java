package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPSystemAdministrator;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;

public class DirectoryServer {
    public DomainPool domainPool;
    private String administratorPassword;
    private String serverIp;

    public DirectoryServer(String serverIp, String administratorPassword)
    {
        this.serverIp = serverIp;
        this.administratorPassword = administratorPassword;
        this.domainPool = new DomainPool();
    }


}
