package net.vespotok.fujin_domains.directory_service.credential_provider;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.directory_service.DomainPool;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.json.JSONObject;

public class DomainDispatcher {
    private DomainPool domainPool;
    private Logging l;
    public DomainDispatcher(DirectoryServer directoryServer)
    {
        this.domainPool = directoryServer.domainPool;
        l = new Logging(LoggingLevel.print, new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "Domain Dispatcher");
        l.log("Created a Domain Dispatcher serving " + directoryServer.getServerAddress() + ".");
    }

    public String getDomainFromUsername(String username) throws Exception {
        if((username.contains("@") && username.contains(".")) || username.contains("\\"))
        {
            LDAPDomainName domainName = parseDomain(username);
            l.log("To search "+domainPool.getDomainNamesList().length+" entities.");
            l.log("Getting: " + domainName.toWin2000Style());
            if(domainPool.domainExists(domainName))
            {

                LDAPDomain domain = domainPool.getDomainByDomainName(domainName);
                l.success("Found: " + domain.getDomainName().toWin2000Style());
                return successJSON(domain, username);
            }
            l.error("Not found: " + domainName.toWin2000Style());
        }


        return errorJSON();
    }

    public String getDomains()
    {
        JSONObject obj = new JSONObject();
        for(String domain : domainPool.getDomainNamesList())
        {
            JSONObject innerobj = new JSONObject();
            LDAPDomain d = domainPool.getDomainByDomainName(domain);
            innerobj.put("domainName", d.getDomainName().toWin2000Style());
            innerobj.put("samDomainName", d.getDomainName().toNT4Style());
            innerobj.put("dn", d.getDomainName().toDN());
            innerobj.put("organizationName", d.getOrganizationName());
            innerobj.put("organizationLogo", d.getOrganizationLogoURL());
            obj.put(domain,innerobj);
        }
        JSONObject returnObject = new JSONObject();

        returnObject.put("status", "success");
        returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
        returnObject.put("domains", obj);

        return returnObject.toString();
    }

    private String successJSON(LDAPDomain domain, String username)
    {
        LDAPObject userObject = domain.getObjectByUserPrincipalName(parseUsername(username));

        JSONObject user = new JSONObject();
        if(userObject != null)
        {
            user.put("name", userObject.getAttributeValue(LDAPAttributeEnum.cn));
            user.put("samLogin", domain.getDomainName().toNT4Style()+"\\"+userObject.getAttributeValue(LDAPAttributeEnum.userPrincipalName));
        }


        JSONObject obj = new JSONObject();
        obj.put("domainName", domain.getDomainName().toWin2000Style());
        obj.put("samDomainName", domain.getDomainName().toNT4Style());
        obj.put("dn", domain.getDomainName().toDN());
        obj.put("organizationName", domain.getOrganizationName());
        obj.put("organizationLogo", domain.getOrganizationLogoURL());
        JSONObject returnObject = new JSONObject();
        returnObject.put("status", "success");
        returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
        returnObject.put("domain", obj);
        returnObject.put("user", user);

        return returnObject.toString();
    }

    private String errorJSON()
    {
        JSONObject returnObject = new JSONObject();
        returnObject.put("status", "error");
        returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");

        return returnObject.toString();
    }

    public LDAPDomainName parseDomain(String username)
    {
        //userPrincipalName
        if(username.contains("@"))
        {
            LDAPDomainName domain = new LDAPDomainName(username.split("@")[1], LDAPDomainNameTypeEnum.Win2000Style);
            l.log("Parsed domain " +domain.toWin2000Style() + " ("+domain.toNT4Style()+") from userPrincipalName style");
            return domain;
        }
        //samAccountName
        if(username.contains("\\"))
        {
            LDAPDomainName domain = new LDAPDomainName(username.split("\\\\")[0], LDAPDomainNameTypeEnum.NT4Style);
            l.log("Parsed domain " +domain.toWin2000Style() + " ("+domain.toNT4Style()+") from SAM style username");
            return domain;
        }
        return null;
    }
    private String parseUsername(String username)
    {
        //userPrincipalName
        if(username.contains("@"))
        {
            return username.split("@")[0];
        }
        //samAccountName
        if(username.contains("\\"))
        {
            return username.split("\\\\")[1];
        }
        return null;
    }
}
