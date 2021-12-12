package net.vespotok.fujin_domains.directory_service.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.directory_service.credential_provider.DomainDispatcher;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DomainDispatcherController {
    @Autowired
    private DirectoryServer directoryServer;
    private Logging l;
    private DomainDispatcher domainDispatcher;

    public DomainDispatcherController(DirectoryServer directoryServer) {
        this.directoryServer = directoryServer;
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Domain Dispatcher");
        this.domainDispatcher = directoryServer.domainDispatcher;
        l.log("Registered new Domain Dispatcher Controller on server "+directoryServer.getServerAddress()+".");
    }

    @RequestMapping(value = "/api/v1/domain", method = RequestMethod.GET, produces = "application/json", params = {"username"})
    public String getDomain(@RequestParam("username") String username) throws Exception {
        return domainDispatcher.getDomainFromUsername(username);
    }

    @RequestMapping(value = "/api/v1/domainlist", method = RequestMethod.GET, produces = "application/json")
    public String getDomains()  {
        return domainDispatcher.getDomains();
    }
}
