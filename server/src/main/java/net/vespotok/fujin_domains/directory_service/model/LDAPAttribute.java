package net.vespotok.fujin_domains.directory_service.model;

import javax.persistence.*;

import static java.lang.Integer.parseInt;

@Entity
public class LDAPAttribute {

    @Enumerated(EnumType.STRING)
    @Column(name="attributeName")
    private LDAPAttributeEnum attributeName;

    @Column(name="attributeValue")
    private String attributeValue;

    @ManyToOne
    @JoinColumn(name = "object_id")
    protected LDAPObject ldapObject;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public LDAPAttribute(LDAPAttributeEnum attributeNameEnum, LDAPObject object)
    {
        this.attributeName = attributeNameEnum;
        this.ldapObject = object;
    }

    public LDAPAttribute(LDAPAttributeEnum attributeNameEnum, String attributeValue, LDAPObject object)
    {
        this.attributeName = attributeNameEnum;
        this.attributeValue = attributeValue;
        this.ldapObject = object;
    }
    public LDAPAttribute(String attributeName, LDAPObject object)
    {
        this.attributeName = LDAPAttributeEnum.valueOf(attributeName);
        this.ldapObject = object;
    }

    public LDAPAttribute(String attributeName, String attributeValue, LDAPObject object)
    {
        this.attributeName = LDAPAttributeEnum.valueOf(attributeName);
        this.attributeValue = attributeValue;
        this.ldapObject = object;
    }

    public LDAPAttribute() {

    }

    public String getAttributeName() {
        return attributeName.name();
    }

    public void setAttributeName(LDAPAttributeEnum attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = LDAPAttributeEnum.valueOf(attributeName);
    }

    public String getAttributeValueString() {
        return attributeValue;
    }

    public int getAttributeValueInt() {
        return parseInt(attributeValue);
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public LDAPObject getLdapObject() {
        return ldapObject;
    }

    public void setLdapObject(LDAPObject ldapObject) {
        this.ldapObject = ldapObject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
