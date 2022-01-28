package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;

import java.util.Iterator;

public class UserObject extends LDAPObject {

    private String username;

    public UserObject(String name, String surname, String username, String password, String telephoneNumber)
    {
        this.username = username;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person"));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name + " " + surname));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.name, name));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.sn, surname));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPassword, password));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.telephoneNumber, telephoneNumber));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPrincipalName, username));

    }

    public UserObject(JSONObject jsonObject)
    {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            l.log("Adding " + key + " attribute with value "+jsonObject.getString(key));
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.valueOf(key), jsonObject.getString(key)));
        }
        this.username = this.getAttributeValue(LDAPAttributeEnum.userPrincipalName);
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person"));
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, "uid="+this.username+","+this.domainName.toDN()));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");
    }

    public UserObject(String name, String surname, String username, String password, String telephoneNumber, LDAPDomainName domainName)
    {
        this.username = username;
        this.domainName = domainName;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, "uid="+username+","+this.domainName.toDN()));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person"));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name + " " + surname));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.name, name));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.sn, surname));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPassword, password));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.telephoneNumber, telephoneNumber));
    }

    public boolean checkPassword(String password)
    {
        if(password.equals(this.getAttributeValue(LDAPAttributeEnum.userPassword)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getName()
    {
        return this.getAttributeValue(LDAPAttributeEnum.name);
    }

    public String getFullName()
    {
        return this.getAttributeValue(LDAPAttributeEnum.cn);
    }

    public String getDN()
    {
        return this.getAttributeValue(LDAPAttributeEnum.dn);
    }
}
