package net.vespotok.fujin_domains.directory_service.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.directory_service.credential_provider.Credential;
import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
public class DirectoryController {
    @Autowired
    private DirectoryServer directoryServer;
    private Logging l;
    public DirectoryController(DirectoryServer directoryServer)
    {
        this.directoryServer = directoryServer;
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style),"Directory Query");
        l.log("Registered new Directory Server serving directory queries on server " + directoryServer.getServerAddress());
    }
    @RequestMapping(value="/api/v1/search", method = RequestMethod.GET, produces = "application/json", params = {"q", "domain", "auth"})
    public String searchByNames(@RequestParam("q") String query,@RequestParam("domain") String domainName,@RequestParam("auth") String authToken)
    {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            ArrayList<LDAPObject> results = targetDomain.searchObjectsByName(query);
            if(results.size() == 0)
            {
                results.add(targetDomain.getObjectByDn(query));
            }
            JSONObject object = new JSONObject();
            for(LDAPObject ldapObject : results)
            {
                object.put(ldapObject.getDn(), ldapObject.toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("namingcontext", targetDomain.getDomainName().toDN());
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value="/api/v1/sids", method = RequestMethod.GET, produces = "application/json", params = {"q", "domain", "auth"})
    public String searchBySIDs(@RequestParam("q") String query,@RequestParam("domain") String domainName,@RequestParam("auth") String authToken)
    {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            ArrayList<LDAPObject> results = targetDomain.getObjectsBySIDs(query);
            JSONObject object = new JSONObject();
            for(LDAPObject ldapObject : results)
            {
                object.put(ldapObject.getDN(), ldapObject.toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("namingcontext", targetDomain.getDomainName().toDN());
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/getLDIF", method = RequestMethod.GET, produces = "application/json", params = {"domain", "auth", "dn"})
    public String getLDIF(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("dn") String dn) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            String object = "";
            object = targetDomain.getObjectByDn(dn).toLDIF();
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/users", method = RequestMethod.GET, produces = "application/json", params = {"domain", "auth"})
    public String getUsers(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            ArrayList<LDAPObject> results = targetDomain.getAllUsers();
            JSONObject object = new JSONObject();
            for(LDAPObject ldapObject : results)
            {
                object.put(ldapObject.getDn(), ldapObject.toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/user", method = RequestMethod.GET, produces = "application/json", params = {"domain", "auth"})
    public String getUser(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            ArrayList<LDAPObject> results = targetDomain.getAllUsers();
            JSONObject object = new JSONObject();
            for(LDAPObject ldapObject : results)
            {
                object.put(ldapObject.getDn(), ldapObject.toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }

    @RequestMapping(value = "/api/v1/groups", method = RequestMethod.GET, produces = "application/json", params = {"domain", "auth"})
    public String getGroups(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            ArrayList<LDAPObject> results = targetDomain.getAllGroups();
            JSONObject object = new JSONObject();
            for(LDAPObject ldapObject : results)
            {
                object.put(ldapObject.getDn(), ldapObject.toJSONObject());
            }
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("data", object);
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            return returnObject.toString();
        }
        return "401";
    }
    @RequestMapping(value = "/api/v1/change", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "dn", "data"})
    public String changeObject(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("dn") String dn,@RequestParam("data") String data) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
           try {
                JSONObject dataParse = new JSONObject(data);
                Iterator<String> keys = dataParse.keys();
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                l.log("Preparing to change " + dataParse.length() + " attributes. User " + checkUsr.getUsername() + " object " + targetDomain.getObjectByDn(dn).getDn());

                while (keys.hasNext()) {

                    String key = keys.next();
                    l.log("Changing " + key + " attribute.");
                    l.log("KEY: " + LDAPAttributeEnum.valueOf(key));
                    l.log("VAL: " + dataParse.getString(key));
                    l.log("USR: " + checkUsr.getSID());

                    targetDomain.changeObject(targetDomain.getObjectByDn(dn), LDAPAttributeEnum.valueOf(key), dataParse.getString(key), checkUsr);
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
    @RequestMapping(value = "/api/v1/delete", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "dn"})
    public String deleteObject(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("dn") String dn) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                l.log("Preparing to delete " + dn + " object. User " + checkUsr.getUsername());
                targetDomain.removeObject(targetDomain.getObjectByDn(dn), checkUsr);

            }
            catch (Exception e){
                l.error("Error while trying to delete an object. " + e);
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
    @RequestMapping(value = "/api/v1/removeFrom", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "dn", "dnToRemove"})
    public String removeFrom(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("dn") String dn,@RequestParam("dnToRemove") String dnToRemove) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                l.log("Preparing to delete " + dn + " object. User " + checkUsr.getUsername());
                targetDomain.removeMember(targetDomain.getObjectByDn(dnToRemove), targetDomain.getObjectByDn(dn), checkUsr);

            }
            catch (Exception e){
                l.error("Error while trying to delete an object. " + e);
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
    @RequestMapping(value = "/api/v1/addTo", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "dn", "dnToAdd"})
    public String addTo(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("dn") String dn,@RequestParam("dnToAdd") String dnToAdd) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                l.log("Preparing to add " + dn + " object. User " + checkUsr.getUsername());
                targetDomain.memberOf(targetDomain.getObjectByDn(dnToAdd), targetDomain.getObjectByDn(dn), checkUsr);

            }
            catch (Exception e){
                l.error("Error while trying to add an object. " + e);
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
    @RequestMapping(value = "/api/v1/add", method = RequestMethod.POST, produces = "application/json", params = {"domain", "auth", "data", "objectclass"})
    public String addObject(@RequestParam("domain") String domainName,@RequestParam("auth") String authToken,@RequestParam("data") String data,@RequestParam("objectclass") String objectclass) throws Exception {
        LDAPDomain targetDomain = this.directoryServer.domainPool.getDomainByDomainName(domainName);
        CredentialProvider credentialProvider = targetDomain.getCredentialProvider();
        Credential credential = credentialProvider.getCredential(authToken);

        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                LDAPUser checkUsr = new LDAPUser(targetDomain);
                checkUsr.loginInternal(credential.getUser());
                JSONObject jsonObject = new JSONObject(data);
                l.log("Preparing to add a new object. User " + checkUsr.getUsername());
                switch (objectclass)
                {
                    case "person":
                        LDAPObject userObject = new UserObject(jsonObject, targetDomain);
                        targetDomain.addObject(userObject, checkUsr);
                    break;
                }
            }
            catch (Exception e){
                l.error("Error while trying to add an object. " + e);
                message = "Stala se chyba, zřejmě nemáte právo přidávat objekty do adresáře. Další informace: " + e;
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