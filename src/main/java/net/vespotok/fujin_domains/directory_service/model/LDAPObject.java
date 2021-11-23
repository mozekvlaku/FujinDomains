package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.model.LDAPAccessRight;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LDAPObject {
    protected ArrayList<LDAPAttribute> attributeArray;
    protected ArrayList<LDAPAccessRight> accessRights;
    protected LDAPDomainName domainName;

    public LDAPObject() {
        this.attributeArray = new ArrayList<LDAPAttribute>();
        this.accessRights = new ArrayList<LDAPAccessRight>();
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
    }

    public void addAccessRight(LDAPAccessRight accessRight)
    {
        this.accessRights.add(accessRight);
    }

    public String getDN()
    {
        return getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }

    public boolean hasRightsToModify(String username)
    {
        String checkUid = "uid="+ username + "," + domainName.toDN();
        for(LDAPAccessRight right : accessRights)
        {
            String uid = right.getUid();
            LDAPAccessRightEnum accessRight = right.getLdapAccessRight();

            if((Objects.equals(uid, checkUid)) && (accessRight.name().equals(LDAPAccessRightEnum.modifyDelete.name())))
            {
                return true;
            }
            if(Objects.equals(uid, checkUid) && (accessRight.name().equals(LDAPAccessRightEnum.addModifyDelete.name())))
            {
                return true;
            }
            if(Objects.equals(uid, checkUid) && (accessRight.name().equals(LDAPAccessRightEnum.administrator.name())))
            {
                return true;
            }
        }
        return false;
    }

    public void addAttribute(LDAPAttribute attribute)
    {
        this.attributeArray.add(attribute);
    }

    public boolean changeAttribute(LDAPAttributeEnum attributeName, String newValue)
    {
        for(LDAPAttribute attribute : attributeArray)
        {
            if(attribute.getAttributeName() == attributeName.name())
            {
                attribute.setAttributeValue(newValue);
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        return false;
    }

    public boolean appendAttribute(LDAPAttributeEnum attributeName, String newValue)
    {
        for(LDAPAttribute attribute : attributeArray)
        {
            if(attribute.getAttributeName() == attributeName.name())
            {
                newValue += ("," + attribute.getAttributeValueString());
                attribute.setAttributeValue(newValue);
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        return false;
    }

    public LDAPAttribute getAttribute(LDAPAttributeEnum attributeName)
    {
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for(int i = 0; i<arrayOfAttributes.length; i++)
        {
            if(arrayOfAttributes[i].getAttributeName() == attributeName.name())
            {
                return arrayOfAttributes[i];
            }
        }
        return null;
    }
    public String getAttributeValue(LDAPAttributeEnum attributeName)
    {
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for(int i = 0; i<arrayOfAttributes.length; i++)
        {
            if(arrayOfAttributes[i].getAttributeName() == attributeName.name())
            {
                return arrayOfAttributes[i].getAttributeValueString();
            }
        }
        return null;
    }

    public String toLDIF()
    {
        String LDIFString = "";
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for(int i = 0; i<arrayOfAttributes.length; i++)
        {
            LDIFString += (arrayOfAttributes[i].getAttributeName() + ": " + arrayOfAttributes[i].getAttributeValueString() + "\n");
        }
        return LDIFString;
    }

}
