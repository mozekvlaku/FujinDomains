package net.vespotok.fujin_domains.directory_service.model;

public class LDAPAccessRight {
    private String uid;
    private LDAPAccessRightEnum ldapAccessRight;

    public LDAPAccessRight(String uid, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.uid = uid;
    }

    public LDAPAccessRight(LDAPUser ldapUser, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.uid = ldapUser.getDn();
    }

    public String getUid() {
        return uid;
    }

    public LDAPAccessRightEnum getLdapAccessRight() {
        return ldapAccessRight;
    }
}
