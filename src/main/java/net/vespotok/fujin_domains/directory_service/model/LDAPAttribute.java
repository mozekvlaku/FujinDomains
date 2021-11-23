package net.vespotok.fujin_domains.directory_service.model;

import static java.lang.Integer.parseInt;

public class LDAPAttribute {


    private LDAPAttributeEnum attributeName;
    private String attributeValue;

    public LDAPAttribute(LDAPAttributeEnum attributeNameEnum)
    {
        this.attributeName = attributeNameEnum;
    }

    public LDAPAttribute(LDAPAttributeEnum attributeNameEnum, String attributeValue)
    {
        this.attributeName = attributeNameEnum;
        this.attributeValue = attributeValue;
    }
    public LDAPAttribute(String attributeName)
    {
        this.attributeName = LDAPAttributeEnum.valueOf(attributeName);
    }

    public LDAPAttribute(String attributeName, String attributeValue)
    {
        this.attributeName = LDAPAttributeEnum.valueOf(attributeName);
        this.attributeValue = attributeValue;
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

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
