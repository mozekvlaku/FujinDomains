package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class LDAPUser {
    private String username;
    private String password;
    protected LDAPDomain loginDomain;
    protected UserObject userObject;
    protected String dn;
    protected String SID;

    public LDAPUser(LDAPDomain loginDomain)
    {
        this.loginDomain = loginDomain;
    }

    public boolean login(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.username = username;
        this.password = password;

        userObject = (UserObject) loginDomain.getObjectByUserPrincipalName(parseUsername(username));

        String userPassword = userObject.getAttributeValue(LDAPAttributeEnum.userPassword);

        if(userObject.checkPassword(password))
        {
            dn = userObject.getDN();
            return true;
        }
        return false;
    }

    public boolean loginInternal(LDAPObject object)
    {
        this.username = object.getAttribute(LDAPAttributeEnum.userPrincipalName).getAttributeValueString();
        this.password = object.getAttribute(LDAPAttributeEnum.userPassword).getAttributeValueString();
        userObject = (UserObject) object;
        dn = userObject.getDN();
        return true;
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

    public String getDn() {
        if(userObject.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString() == null)
            return userObject.getDn();
        else
            return userObject.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }
    public String getSID() {
        if(userObject.getAttribute(LDAPAttributeEnum.objectSid).getAttributeValueString() == null)
            return userObject.getSID();
        else
            return userObject.getAttribute(LDAPAttributeEnum.objectSid).getAttributeValueString();
    }

    public String getDN() {
        return userObject.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }

    public String getUsername() {
        return username;
    }
}
