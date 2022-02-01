package net.vespotok.fujin_domains.api;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.credential_provider.Credential;
import net.vespotok.fujin_domains.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.credential_provider.JSONTypeEnum;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class UserController {
    @Autowired
    private DirectoryServer directoryServer;


    public UserController(DirectoryServer directoryServer) {
        this.directoryServer = directoryServer;
        Logging l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Login System");
        l.log("Registered new Login Controller on server "+directoryServer.getServerAddress()+". System ready.");
    }

    @RequestMapping(value = "/api/v1/login", method = RequestMethod.GET, produces = "application/json", params = {"domain", "username", "password"})
    public String tryLogin(@RequestParam("domain") String domain,@RequestParam("password") String password,@RequestParam("username") String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
        LDAPDomain ldapDomain = this.directoryServer.domainPool.getDomainByDomainName(domain);
        CredentialProvider cp = ldapDomain.getCredentialProvider();
        cp.setLdapDomain(ldapDomain);
        ldapDomain.setDomainName(ldapDomain.getDomainName());
        Logging l = new Logging(LoggingLevel.print, ldapDomain.getDomainName(), "Login System");
        l.log("Trying to login with username " + username);
        Credential login = cp.attemptLoginByPrincipalName(username,password);
        if(login != null)
        {
            l.success("Successful login!");
            return cp.getUserAccountJSON(JSONTypeEnum.directoryAttributes, login);
        }
        else
        {
            l.error("Wrong credentials or user not exists!");
            return cp.getUnsuccessfulJSON("Wrong credentials or user not exists!");
        }
    }

    @RequestMapping(value = "/api/v1/user", method = RequestMethod.GET, produces = "application/json", params = {"domain","token", "type"})
    public String getUser(@RequestParam("domain") String domain,@RequestParam("token") String token, @RequestParam(name="type", required = false) String type) {
        LDAPDomain ldapDomain = this.directoryServer.domainPool.getDomainByDomainName(domain);
        CredentialProvider cp = ldapDomain.getCredentialProvider();
        Logging l = new Logging(LoggingLevel.print, ldapDomain.getDomainName(), "Login System");
        Credential user = cp.getCredential(token);
        if(user != null)
        {
            l.log("Getting token " + user.getUUID());
            switch (type)
            {
                case "fujin":
                    return cp.getUserAccountJSON(JSONTypeEnum.fujinAccount, user);

                case "legacyFujin":
                    return cp.getUserAccountJSON(JSONTypeEnum.fujinAccountLegacy, user);

                default:
                    return cp.getUserAccountJSON(JSONTypeEnum.directoryAttributes, user);

            }

        }
        else
        {
            l.error("Wrong token!");
            return cp.getUnsuccessfulJSON("Wrong token! " + token);
        }
    }

    @RequestMapping(value = "/api/v1/logout", method = RequestMethod.GET, produces = "application/json", params = {"domain","token"})
    public String tryLogout(@RequestParam("domain") String domain,@RequestParam("token") String token) {
        LDAPDomain ldapDomain = this.directoryServer.domainPool.getDomainByDomainName(domain);
        CredentialProvider cp = ldapDomain.getCredentialProvider();
        Logging l = new Logging(LoggingLevel.print, ldapDomain.getDomainName(), "Login System");
        Credential user = cp.getCredential(token);
        if(user != null)
        {
            l.log("Getting token " + user.getUUID());
            l.log("Logging out user " + user.getUser().getDN());
            cp.logout(user);
            return cp.getLogoutJSON("Logged out successfully.");

        }
        else
        {
            l.error("Wrong token!");
            return cp.getUnsuccessfulJSON("Wrong token! " + token);
        }
    }

    @RequestMapping(value = "/api/v1/resetpassword", method = RequestMethod.POST, produces = "application/json", params = {"auth", "data"})
    public String resetPassword(@RequestParam("auth") String authToken,@RequestParam("data") String data) throws Exception {
        Credential credential = this.directoryServer.domainPool.getDomainCredentialByToken(authToken);
        if(credential != null)
        {
            var message = "Vše je v pořádku.";
            var status = "success";
            try {
                JSONObject dataParse = new JSONObject(data);
                credential.getUser().changePassword(dataParse.getString("old"), dataParse.getString("new1"), dataParse.getString("new2"));
            }
            catch (Exception e){
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
