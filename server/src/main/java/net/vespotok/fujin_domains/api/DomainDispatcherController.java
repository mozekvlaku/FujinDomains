package net.vespotok.fujin_domains.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.credential_provider.Credential;
import net.vespotok.fujin_domains.credential_provider.DomainDispatcher;
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

    @RequestMapping(value = "/api/v1/domainlogins", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth"})
    public String getDomainLogins(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {

        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        Credential credential = this.directoryServer.domainPool.getDomainCredentialByToken(authToken);

        if(credential != null)
        {
            ArrayList<Credential> credentials = new ArrayList<>();

            for (Credential cred : targetDomain.getCredentialProvider().getCredentials())
            {
                credentials.add(cred);
            }


            JSONObject object = new JSONObject();
            for(Credential cred : credentials)
            {
                object.put(cred.getUser().getDN(), cred.getUser().toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/changedomain", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "data"})
    public String changeDomain(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("data") String data) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        Credential credential = this.directoryServer.domainPool.getDomainCredentialByToken(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {

                JSONObject dataParse = new JSONObject(data);

                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                targetDomain.changeDomain(dataParse.getString("organizationName"), dataParse.getString("organizationLogo"), checkUsr);
                l.log("Preparing to change domain. User " + checkUsr.getUsername());

            }
            catch (Exception e){
                l.error("Error while trying to change an object. " + e);
                message = "Stala se chyba, zřejmě nemáte přístup k objektu. Další informace: " + e;
                status = "error";
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", status);
            returnObject.put("message", message);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/removedomain", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth"})
    public String changeDomain(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        Credential credential = this.directoryServer.domainPool.getDomainCredentialByToken(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                if(targetDomain.getAh().hasRightsToServerManipulate(checkUsr))
                {
                    this.directoryServer.domainPool.removeDomain(targetDomain);
                }
            }
            catch (Exception e){
                l.error("Error while trying to change an object. " + e);
                message = "Stala se chyba, zřejmě nemáte přístup k objektu. Další informace: " + e;
                status = "error";
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", status);
            returnObject.put("message", message);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
}
