package net.vespotok.fujin_domains.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.credential_provider.Credential;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
    @RequestMapping(value = "/api/v1/server", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth"})
    public String getServer(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {

        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        Credential credential = this.directoryServer.domainPool.getDomainCredentialByToken(authToken);

        if(credential != null)
        {
            ArrayList<Credential> credentials = new ArrayList<>();
            for(LDAPDomain domain : this.directoryServer.domainPool.getDomains())
            {
                for (Credential cred : domain.getCredentialProvider().getCredentials())
                {
                    credentials.add(cred);
                }
            }

            JSONObject object = new JSONObject();
            for(Credential cred : credentials)
            {
                object.put(cred.getUser().getDN(), cred.getUser().toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("address", this.directoryServer.getServerAddress());
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            returnObject.put("subversion", "Fujin Directory and Credential Provider Service");
            return returnObject.toString();
        }
        return "401";
    }
}
