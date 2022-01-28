package net.vespotok.fujin_domains.directory_service.model;

public class LDAPAccessRight {
    private String SID;
    private LDAPAccessRightEnum ldapAccessRight;
    private String username;

    public LDAPAccessRight(String SID, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.SID = SID;
        this.username = SID;
    }

    public LDAPAccessRight(LDAPUser ldapUser, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.SID = ldapUser.getSID();
        this.username = ldapUser.getUsername();

    }

    public String getSID() {
        return SID;
    }
    public String getUsername() {
        return username;
    }

    public LDAPAccessRightEnum getLdapAccessRight() {
        return ldapAccessRight;
    }
}
