package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
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
    protected String dn;

    protected Logging l;

    public LDAPObject() {
        this.attributeArray = new ArrayList<>();
        this.accessRights = new ArrayList<>();
        l = new Logging(LoggingLevel.print);
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        l = new Logging(LoggingLevel.print, domainName, "Directory Object");
    }

    public LDAPDomainName getDomainName()
    {
        return this.domainName;
    }

    public void addAccessRight(LDAPAccessRight accessRight)
    {
        this.accessRights.add(accessRight);
    }

    public void addToObject(LDAPObject object)
    {
        String lastDn = object.getDN().split(",")[0];
        String[] thisDn = getAttributeValue(LDAPAttributeEnum.dn).split(",");
        ArrayList<String> resultDn = new ArrayList<>();
        for(int i = 0; i < thisDn.length+1; i++)
        {
            if(i == 0)
            {
                resultDn.add(thisDn[0]);
            }
            else
            {
                if(i == 1)
                {
                    resultDn.add(lastDn);
                }
                else
                {
                    resultDn.add(thisDn[i-1]);
                }
            }
        }
        changeAttribute(LDAPAttributeEnum.dn, String.join(",", resultDn.toArray(new String[0])));
    }

    public LDAPAttribute[] getAttributes()
    {
        return this.attributeArray.toArray(new LDAPAttribute[0]);
    }

    public String getDN()
    {
        if(getAttribute(LDAPAttributeEnum.dn).getAttributeValueString() == null)
            return "that is initializing";
            else
                return getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }

    public boolean hasRightsToModify(LDAPUser ldapUser)
    {
        if(ldapUser.getClass() == LDAPSystemAdministrator.class)
        {
            return true;
        }
        String checkUid = ldapUser.getDn();
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
        l.log("Adding " + attribute.getAttributeName() + ", value " + attribute.getAttributeValueString());
    }

    public boolean changeAttribute(LDAPAttributeEnum attributeName, String newValue)
    {
        for(LDAPAttribute attribute : attributeArray)
        {
            if(Objects.equals(attribute.getAttributeName(), attributeName.name()))
            {
                attribute.setAttributeValue(newValue);
                l.log("Updating " + attribute.getAttributeName() + " setting value " + attribute.getAttributeValueString()+ " on object " + getDN());
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        l.log("Adding " + attributeName + ", value " + newValue);

        return false;
    }

    public String getMemberships()
    {
        String memberships;
        ArrayList<String> membershipArray = new ArrayList<>();
        for(LDAPAttribute attribute : attributeArray)
        {
            if(Objects.equals(attribute.getAttributeName(), "memberOf"))
            {
                membershipArray.add(attribute.getAttributeValueString().split(",")[0].split("=")[1]);
            }
        }
        memberships = String.join(",", membershipArray.toArray(new String[0]));
        return memberships;
    }

    public boolean appendAttribute(LDAPAttributeEnum attributeName, String newValue)
    {
        for(LDAPAttribute attribute : attributeArray)
        {
            if(Objects.equals(attribute.getAttributeName(), attributeName.name()))
            {
                newValue += ("," + attribute.getAttributeValueString());
                attribute.setAttributeValue(newValue);
                l.log("Updating " + attribute.getAttributeName() + " appending value " + newValue + " to object " + getDN());
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        return false;
    }

    public LDAPAttribute getAttribute(LDAPAttributeEnum attributeName)
    {
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for (LDAPAttribute arrayOfAttribute : arrayOfAttributes) {
            if (Objects.equals(arrayOfAttribute.getAttributeName(), attributeName.name())) {
                return arrayOfAttribute;
            }
        }
        return null;
    }

    public String getAttributeValue(LDAPAttributeEnum attributeName)
    {
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for (LDAPAttribute arrayOfAttribute : arrayOfAttributes) {
            if (Objects.equals(arrayOfAttribute.getAttributeName(), attributeName.name())) {
                return arrayOfAttribute.getAttributeValueString();
            }
        }
        return null;
    }

    public String toLDIF()
    {
        StringBuilder LDIFString = new StringBuilder();
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for (LDAPAttribute arrayOfAttribute : arrayOfAttributes) {
            LDIFString.append(arrayOfAttribute.getAttributeName()).append(": ").append(arrayOfAttribute.getAttributeValueString()).append("\n");
        }
        return LDIFString.toString();
    }

}
