package net.vespotok.fujin_domains.directory_service.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LDAPDomain {
    private ArrayList<LDAPObject> domainObjects;
    private LDAPDomainName domainName;
    private String organizationName;
    private String organizationLogoURL;

    public LDAPDomain(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.organizationName = "No name domain";
        domainObjects = new ArrayList<>();
    }

    public LDAPDomain(LDAPDomainName domainName, String organizationName, String organizationLogoURL)
    {
        this.domainName = domainName;
        this.organizationName = organizationName;
        this.organizationLogoURL = organizationLogoURL;
        domainObjects = new ArrayList<>();
    }

    public void addObject(LDAPObject object, LDAPUser ldapUser)
    {
        object.setDomainName(this.domainName);
        object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.administrator));
        object.addAccessRight(new LDAPAccessRight(ldapUser, LDAPAccessRightEnum.addModifyDelete));
        domainObjects.add(object);
    }

    public void changeObject(LDAPObject object, LDAPAttributeEnum attributeToChange, String newValue, LDAPUser ldapUser) throws Exception {
        if(object.hasRightsToModify(ldapUser))
        {
            object.changeAttribute(attributeToChange, newValue);
        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void memberOf(LDAPObject object, LDAPObject what, LDAPUser ldapUser) throws Exception {
        if(object.hasRightsToModify(ldapUser))
        {
            object.addAttribute(new LDAPAttribute(LDAPAttributeEnum.memberOf, what.getDN()));
        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }


    public LDAPObject getObjectByDn(String dn)
    {
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.dn).getAttributeValueString(), dn))
            {
                return object;
            }
        }
        return null;
    }

    public LDAPObject getObjectByUserPrincipalName(String userPrincipalName)
    {
        for(LDAPObject object : domainObjects)
        {
            if(Objects.equals(object.getAttribute(LDAPAttributeEnum.userPrincipalName).getAttributeValueString(), userPrincipalName))
            {
                return object;
            }
        }
        return null;
    }

    public ArrayList<LDAPObject> searchObjectsByName(String query)
    {
        ArrayList<LDAPObject> returnObjects = new ArrayList<>();
        query = query.toLowerCase();
        for(LDAPObject object : domainObjects)
        {
            LDAPAttribute name = object.getAttribute(LDAPAttributeEnum.name);
            LDAPAttribute sn = object.getAttribute(LDAPAttributeEnum.sn);
            LDAPAttribute cn = object.getAttribute(LDAPAttributeEnum.cn);
            LDAPAttribute commonName = object.getAttribute(LDAPAttributeEnum.commonName);
            if(
                (cn != null && cn.getAttributeValueString().toLowerCase().contains(query)) ||
                (sn != null && sn.getAttributeValueString().toLowerCase().contains(query)) ||
                (name != null && name.getAttributeValueString().toLowerCase().contains(query))||
                (commonName != null && commonName.getAttributeValueString().toLowerCase().contains(query))
            )
            {
                returnObjects.add(object);
            }
        }
        return returnObjects;
    }

    public LDAPDomainName getDomainName()
    {
        return this.domainName;
    }
}
