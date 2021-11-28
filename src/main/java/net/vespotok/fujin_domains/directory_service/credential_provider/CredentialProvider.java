package net.vespotok.fujin_domains.directory_service.credential_provider;

import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.json.JSONObject;

import java.util.Objects;

public class CredentialProvider {
    private LDAPDomain ldapDomain;
    private UserObject loggedInUser;
    private boolean loggedIn = false;

    public CredentialProvider(LDAPDomain ldapDomain)
    {
        this.ldapDomain = ldapDomain;
    }

    public boolean attemptLoginByPrincipalName(String username, String password)
    {
        if(username != "" && password != "")
        {
            loggedInUser = (UserObject) ldapDomain.getObjectByUserPrincipalName(parseUsername(username));
            if(loggedInUser.checkPassword(password))
            {
                loggedIn = true;
                return true;
            }
            loggedInUser = null;
            return false;
        }
        return false;
    }
    public boolean attemptLoginBySAMName(String username, String password)
    {
        if(username != "" && password != "")
        {
            loggedInUser = (UserObject) ldapDomain.getObjectBySAMName(parseUsername(username));
            if(loggedInUser.checkPassword(password))
            {
                loggedIn = true;
                return true;
            }
            loggedInUser = null;
            return false;
        }
        return false;
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
        return username;
    }
    public String getUserAccountJSON(JSONTypeEnum type)
    {
        if(loggedIn)
        {
            JSONObject obj = new JSONObject();
            switch (type)
            {
                case fujinAccountLegacy:
                    obj.put("FJID", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinId));
                    obj.put("USERNAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.userPrincipalName));
                    obj.put("ISACTIVE", 1);
                    obj.put("ADMIN", "yes");
                    obj.put("FJGROUP", "domainUser");
                    obj.put("USERGROUPS", loggedInUser.getMemberships());
                    obj.put("SH", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinSh));
                    obj.put("GUI", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinGui));
                    obj.put("NAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.name));
                    obj.put("SURNAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.sn));
                    obj.put("PROFILE_PIC", "");
                    obj.put("EMAIL", loggedInUser.getAttributeValue(LDAPAttributeEnum.mail));
                    obj.put("PHONE", loggedInUser.getAttributeValue(LDAPAttributeEnum.telephoneNumber));
                    obj.put("ADDRESS_ID", "NULL");
                    obj.put("PERMISSIONS", "NULL");
                    break;
                case fujinAccount:
                    obj.put("FJID", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinId));
                    obj.put("USERNAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.userPrincipalName));
                    obj.put("USERGROUPS", loggedInUser.getMemberships());
                    obj.put("SH", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinSh));
                    obj.put("GUI", loggedInUser.getAttributeValue(LDAPAttributeEnum.fujinGui));
                    obj.put("NAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.name));
                    obj.put("SURNAME", loggedInUser.getAttributeValue(LDAPAttributeEnum.sn));
                    obj.put("PROFILE_PIC", "");
                    obj.put("EMAIL", loggedInUser.getAttributeValue(LDAPAttributeEnum.mail));
                    obj.put("PHONE", loggedInUser.getAttributeValue(LDAPAttributeEnum.telephoneNumber));
                    break;
                case directoryAttributes:
                    for(LDAPAttribute attribute : loggedInUser.getAttributes())
                    {
                        obj.put(attribute.getAttributeName(),attribute.getAttributeValueString());
                    }
                    break;
            }
            return obj.toString();
        }
        return "Not logged in.";
    }
}
