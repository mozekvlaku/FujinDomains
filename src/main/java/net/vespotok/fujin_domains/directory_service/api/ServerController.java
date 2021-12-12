package net.vespotok.fujin_domains.directory_service.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    @Autowired
    private DirectoryServer directoryServer;
    public ServerController(DirectoryServer directoryServer) {
        this.directoryServer = directoryServer;
        Logging l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Login System");
        l.log("Registered new Server Controller on server "+directoryServer.getServerAddress()+".");
    }
    @RequestMapping(value = "/api/v1/info", method = RequestMethod.GET)
    public String getDomain() throws Exception {
        return "Vespotok Fujin Domains 1.0.0";
    }
}
