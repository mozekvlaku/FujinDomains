package net.vespotok.fujin_domains.directory_service.model;

import javax.persistence.*;

@Entity
public class LDAPAccessRight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "SID")
    private String SID;

    @Enumerated(EnumType.STRING)
    @Column(name = "ldapAccessRight")
    private LDAPAccessRightEnum ldapAccessRight;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private LDAPObject ldapObject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LDAPObject getLdapObject() {
        return ldapObject;
    }

    public void setLdapObject(LDAPObject ldapObject) {
        this.ldapObject = ldapObject;
    }

    public LDAPAccessRight(String SID, LDAPAccessRightEnum ldapAccessRight, LDAPObject object)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.SID = SID;
        this.username = SID;
        this.ldapObject = object;
    }

    public LDAPAccessRight(LDAPUser ldapUser, LDAPAccessRightEnum ldapAccessRight, LDAPObject object)
    {
        this.ldapAccessRight = ldapAccessRight;
        this.SID = ldapUser.getSID();
        this.username = ldapUser.getUsername();
        this.ldapObject = object;
    }

    public LDAPAccessRight() {

    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public void setLdapAccessRight(LDAPAccessRightEnum ldapAccessRight) {
        this.ldapAccessRight = ldapAccessRight;
    }

    public void setUsername(String username) {
        this.username = username;
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
