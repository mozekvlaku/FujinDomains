package net.vespotok.fujin_domains.directory_service.model;

public class LDAPAccessRight {
    private String uid;
    private LDAPAccessRightEnum ldapAccessRight;

    public LDAPAccessRight(String uid, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.uid = uid;
    }
    public LDAPAccessRight(String username, LDAPDomainName domain, LDAPAccessRightEnum ldapAccessRight)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.uid = "uid="+ username + "," + domain.toDN();
    }


    public String getUid() {
        return uid;
    }

    public LDAPAccessRightEnum getLdapAccessRight() {
        return ldapAccessRight;
    }
}
