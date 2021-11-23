package net.vespotok.fujin_domains.directory_service.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LDAPDomain {
    private ArrayList<LDAPObject> domainObjects;
    private LDAPDomainName domainName;
    private String organizationName;
    private String organizationLogoURL;

    public LDAPDomain(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.organizationName = "No name domain";
        domainObjects = new ArrayList<LDAPObject>();
    }

    public LDAPDomain(LDAPDomainName domainName, String organizationName, String organizationLogoURL)
    {
        this.domainName = domainName;
        this.organizationName = organizationName;
        this.organizationLogoURL = organizationLogoURL;
        domainObjects = new ArrayList<LDAPObject>();
    }

    public void addObject(LDAPObject object, String creatorUsername)
    {
        object.setDomainName(this.domainName);
        object.addAccessRight(new LDAPAccessRight(creatorUsername, this.domainName, LDAPAccessRightEnum.administrator));
        object.addAccessRight(new LDAPAccessRight(creatorUsername, this.domainName, LDAPAccessRightEnum.addModifyDelete));
        domainObjects.add(object);
    }

    public void changeObject(LDAPObject object, LDAPAttributeEnum attributeToChange, String newValue, String usernameOfChangingPerson) throws Exception {
        if(object.hasRightsToModify(usernameOfChangingPerson))
        {
            object.changeAttribute(attributeToChange, newValue);
        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }

    public void memberOf(LDAPObject object, LDAPObject what, String usernameOfChangingPerson) throws Exception {
        if(object.hasRightsToModify(usernameOfChangingPerson))
        {
            object.addAttribute(new LDAPAttribute(LDAPAttributeEnum.memberOf, what.getDN()));
        }
        else
        {
            throw new Exception("Fujin Domains Exception: You don't have rights to change this directory object.");
        }
    }


    public LDAPObject getObjectByUid(String uid)
    {
        for(LDAPObject object : domainObjects)
        {
            if(object.getAttribute(LDAPAttributeEnum.uid).getAttributeValueString() == uid)
            {
                return object;
            }
        }
        return null;
    }
    public ArrayList<LDAPObject> searchObjectsByName(String query)
    {
        ArrayList<LDAPObject> returnObjects = new ArrayList<LDAPObject>();
        for(LDAPObject object : domainObjects)
        {
            LDAPAttribute name = object.getAttribute(LDAPAttributeEnum.name);
            LDAPAttribute sn = object.getAttribute(LDAPAttributeEnum.sn);
            LDAPAttribute cn = object.getAttribute(LDAPAttributeEnum.cn);
            LDAPAttribute commonName = object.getAttribute(LDAPAttributeEnum.commonName);
            if(
                (cn != null && cn.getAttributeValueString().contains(query)) ||
                (sn != null && sn.getAttributeValueString().contains(query)) ||
                (name != null && name.getAttributeValueString().contains(query))||
                (commonName != null && commonName.getAttributeValueString().contains(query))
            )
            {
                returnObjects.add(object);
            }
        }
        return returnObjects;
    }
}
