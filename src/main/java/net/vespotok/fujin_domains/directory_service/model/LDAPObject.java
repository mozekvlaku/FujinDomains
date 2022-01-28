package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPAccessRight;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LDAPObject {
    protected ArrayList<LDAPAttribute> attributeArray;
    protected ArrayList<LDAPAccessRight> accessRights;
    protected LDAPDomainName domainName;
    protected String dn;
    public String SID;

    protected Logging l;

    public LDAPObject() {
        this.attributeArray = new ArrayList<>();
        this.accessRights = new ArrayList<>();
        l = new Logging(LoggingLevel.print);
        generateSecurityIdentifier();
    }

    public void setDomainName(LDAPDomainName domainName) {
        this.domainName = domainName;
        l = new Logging(LoggingLevel.print, domainName, "Directory Object");
    }

    public LDAPDomainName getDomainName() {
        return this.domainName;
    }

    public void addAccessRight(LDAPAccessRight accessRight) {
        this.accessRights.add(accessRight);
    }

    public void addToObject(LDAPObject object) {
        String lastDn = object.getDN().split(",")[0];
        String[] thisDn = getAttributeValue(LDAPAttributeEnum.dn).split(",");
        ArrayList<String> resultDn = new ArrayList<>();
        for (int i = 0; i < thisDn.length + 1; i++) {
            if (i == 0) {
                resultDn.add(thisDn[0]);
            } else {
                if (i == 1) {
                    resultDn.add(lastDn);
                } else {
                    resultDn.add(thisDn[i - 1]);
                }
            }
        }
        changeAttribute(LDAPAttributeEnum.dn, String.join(",", resultDn.toArray(new String[0])));
    }

    public LDAPAttribute[] getAttributes() {
        return this.attributeArray.toArray(new LDAPAttribute[0]);
    }

    public String getDN() {
        if (getAttribute(LDAPAttributeEnum.dn).getAttributeValueString() == null)
            return "that is initializing";
        else
            return getAttribute(LDAPAttributeEnum.dn).getAttributeValueString();
    }

    public boolean hasRightsToModify(LDAPUser ldapUser) {
        l.log("Checking user " + ldapUser.getUsername() + " on permissions for an object " + this.getDN());

        if (ldapUser.getClass() == LDAPSystemAdministrator.class) {
            l.success("User " + ldapUser.getUsername() + " is administrator, granting.");
            return true;
        }
        String checkSid = ldapUser.getSID();
        for (LDAPAccessRight right : accessRights) {
            String sid = right.getSID();
            LDAPAccessRightEnum accessRight = right.getLdapAccessRight();

            if ((Objects.equals(sid, checkSid)) && (accessRight.name().equals(LDAPAccessRightEnum.modifyDelete.name()))) {
                l.success("User " + ldapUser.getUsername() + " has permissions of " + accessRight.toString() + ", granting.");
                return true;
            }
            if (Objects.equals(sid, checkSid) && (accessRight.name().equals(LDAPAccessRightEnum.addModifyDelete.name()))) {
                l.success("User " + ldapUser.getUsername() + " has permissions of " + accessRight.toString() + ", granting.");
                return true;
            }
            if (Objects.equals(sid, checkSid) && (accessRight.name().equals(LDAPAccessRightEnum.administrator.name()))) {
                l.success("User " + ldapUser.getUsername() + " has permissions of " + accessRight.toString() + ", granting.");
                return true;
            }


        }
        String users = "|";
        for (int i = 0; i < accessRights.size(); i++) {
            users += accessRights.get(i).getSID() + " (" + accessRights.get(i).getLdapAccessRight().toString() + ")|";
        }
        l.error("User " + ldapUser.getUsername() + " does not have permissions to change this object. Only " + users + " have permissions to do anything with this object, stopping.");

        return false;
    }

    public String getSID()
    {
        return this.SID;
    }

    public void addAttribute(LDAPAttribute attribute) {
        this.attributeArray.add(attribute);
        l.log("Adding " + attribute.getAttributeName() + ", value " + attribute.getAttributeValueString());
    }

    public boolean changeAttribute(LDAPAttributeEnum attributeName, String newValue) {
        for (LDAPAttribute attribute : attributeArray) {
            if (Objects.equals(attribute.getAttributeName(), attributeName.name())) {
                attribute.setAttributeValue(newValue);
                l.log("Updating " + attribute.getAttributeName() + " setting value " + attribute.getAttributeValueString() + " on object " + getDN());
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        l.log("Adding " + attributeName + ", value " + newValue);

        return false;
    }

    public String getMemberships() {
        String memberships;
        ArrayList<String> membershipArray = new ArrayList<>();
        for (LDAPAttribute attribute : attributeArray) {
            if (Objects.equals(attribute.getAttributeName(), "memberOf")) {
                membershipArray.add(attribute.getAttributeValueString().split(",")[0].split("=")[1]);
            }
        }
        memberships = String.join(",", membershipArray.toArray(new String[0]));
        return memberships;
    }

    public boolean appendAttribute(LDAPAttributeEnum attributeName, String newValue) {
        for (LDAPAttribute attribute : attributeArray) {
            if (Objects.equals(attribute.getAttributeName(), attributeName.name())) {
                newValue += ("," + attribute.getAttributeValueString());
                attribute.setAttributeValue(newValue);
                l.log("Updating " + attribute.getAttributeName() + " appending value " + newValue + " to object " + getDN());
                return true;
            }
        }
        addAttribute(new LDAPAttribute(attributeName, newValue));
        return false;
    }
    public boolean removeAppendedAttribute(LDAPAttributeEnum attributeName, String value) {
        for (LDAPAttribute attribute : attributeArray) {
            if (Objects.equals(attribute.getAttributeName(), attributeName.name())) {
                l.log("Before: " + attribute.getAttributeValueString());

                String attributes = attribute.getAttributeValueString();
                attributes = attributes.replace(value, "");
                attributes = attributes.replace(",,", ",");
                attribute.setAttributeValue(attributes);
                l.log("After:  " + attributes);

                l.log("Updating " + attribute.getAttributeName() + " removing value " + value + " from object " + getDN());
                return true;
            }
        }
        return false;
    }

    private void generateSecurityIdentifier()
    {
        // Authority: Fujin Domains - 66
        // Subauthority: Broad LDAP object - 33
        // Revision: Fujin Domains custom SID - 9

        String revision = "9";
        String authority = "66";
        String subauthority = "33";
        UUID uuid = UUID.randomUUID();
        String uniqueidentifier = uuid.toString();

        this.SID = "S-" + revision + "-" + authority + "-"+ subauthority + "-" +uniqueidentifier;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectSid, SID));

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
    public String toJSON()
    {
        JSONObject object = new JSONObject();
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for (LDAPAttribute arrayOfAttribute : arrayOfAttributes) {
            object.put(arrayOfAttribute.getAttributeName(), arrayOfAttribute.getAttributeValueString());
        }
        return object.toString();
    }
    public JSONObject toJSONObject()
    {
        JSONObject object = new JSONObject();
        LDAPAttribute[] arrayOfAttributes = attributeArray.toArray(LDAPAttribute[]::new);
        for (LDAPAttribute arrayOfAttribute : arrayOfAttributes) {
            object.put(arrayOfAttribute.getAttributeName(), arrayOfAttribute.getAttributeValueString());
        }
        return object;
    }

}
