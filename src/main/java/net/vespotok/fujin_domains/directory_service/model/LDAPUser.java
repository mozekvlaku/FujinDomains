package net.vespotok.fujin_domains.directory_service.model;

import java.util.Objects;

public class LDAPUser {
    private String username;
    private String password;
    protected LDAPDomain loginDomain;
    protected LDAPObject userObject;
    protected String dn;

    public LDAPUser(LDAPDomain loginDomain)
    {
        this.loginDomain = loginDomain;
    }

    public boolean login(String username, String password)
    {
        this.username = username;
        this.password = password;

        userObject = loginDomain.getObjectByUserPrincipalName(parseUsername(username));

        if(Objects.equals(userObject.getAttribute(LDAPAttributeEnum.userPassword).getAttributeValueString(), password))
        {
            dn = userObject.getDN();
            return true;
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

    public String getDn() {
        return dn;
    }

    public String getDN() {
        return userObject.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }

    public String getUsername() {
        return username;
    }
}
