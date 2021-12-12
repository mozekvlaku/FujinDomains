package net.vespotok.fujin_domains.directory_service.credential_provider;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomain;
import net.vespotok.fujin_domains.directory_service.model.LDAPObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CredentialProvider {
    private LDAPDomain ldapDomain;
    private boolean loggedIn = false;

    private ArrayList<Credential> credentials;

    private Logging l;

    public CredentialProvider(LDAPDomain ldapDomain)
    {
        this.ldapDomain = ldapDomain;
        credentials = new ArrayList<>();
        l = new Logging(LoggingLevel.print, ldapDomain.getDomainName(),"Credential Provider");
        l.log("Created a Credential Provider serving " + ldapDomain.getDomainName().toWin2000Style() + " domain.");
    }

    public Credential attemptLoginByPrincipalName(String username, String password)
    {
        if(username != "" && password != "")
        {
            UserObject loggedInUser = null;
            try {
                loggedInUser = (UserObject) ldapDomain.getObjectByUserPrincipalName(parseUsername(username));
            }
            catch (Exception e)
            {
                l.error(e.getMessage());
            }
            if(loggedInUser != null) {
                if (loggedInUser.checkPassword(password)) {
                    Credential credential;
                    if (this.checkUserIfAlreadyLogged(loggedInUser)) {
                        credential = getUser(loggedInUser);
                        Date now = new Date();
                        long diffM = (Math.abs(now.getTime() - credential.getExpires().getTime())) / 1000 / 60;
                        long diffH = diffM / 60;
                        credential.renew();
                        l.success("Already logged in user " + loggedInUser.getDN() + ", returning credential " + credential.getUUID() + " expiring in " + diffM + " minutes (" + diffH + " hours).");
                    } else {
                        credential = new Credential(loggedInUser);
                        credentials.add(credential);
                        l.success("Granted for " + loggedInUser.getDN());
                    }


                    return credential;
                }

                l.error("Wrong password");
            }
            l.error("Wrong credentials given");
            return null;
        }
        return null;
    }

    public boolean logout(Credential credential)
    {
        this.credentials.remove(credential);
        l.success("Logged off user " + credential.getUser().getDN());
        return true;
    }

    private boolean checkUserIfAlreadyLogged(UserObject object)
    {
        for(Credential credential : credentials)
        {
            if(credential.getUser() == object)
            {
                return true;
            }
        }
        return false;
    }

    private Credential getUser(UserObject object)
    {
        for(Credential credential : credentials)
        {
            if(credential.getUser() == object)
            {
                return credential;
            }
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
        return username;
    }
    public String getUnsuccessfulJSON(String message)
    {
        JSONObject returnObject = new JSONObject();
        returnObject.put("status", "error");
        returnObject.put("message", message);
        returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
        return returnObject.toString();
    }

    public String getLogoutJSON(String message)
    {
        JSONObject returnObject = new JSONObject();
        returnObject.put("status", "success");
        returnObject.put("message", message);
        returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
        return returnObject.toString();
    }

    public Credential getCredential(String token)
    {
        for(Credential credential : credentials)
        {
            if(Objects.equals(credential.getUUID(), token))
            {
                return credential;
            }
        }
        return null;
    }

    public String getUserAccountJSON(JSONTypeEnum type, Credential credential)
    {
        if(credential != null)
        {
            UserObject loggedInUser = credential.getUser();
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
            JSONObject returnObject = new JSONObject();
            returnObject.put("status", "success");
            returnObject.put("version", "Vespotok Fujin Domain Service 1.0.0");
            returnObject.put("token", credential.getUUID());
            returnObject.put("expires", credential.getExpires().toString());
            returnObject.put("user",obj);
            return returnObject.toString();
        }
        return "Not logged in.";
    }
}
