package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;

import javax.persistence.Entity;
import java.util.Iterator;
@Entity
public class UserObject extends LDAPObject {

    private String username;

    public UserObject(String name, String surname, String username, String password, String telephoneNumber, LDAPDomain domain)
    {
        this.username = username;
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person", this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name + " " + surname, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.givenName, name, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.sn, surname, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPassword, password, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPrincipalName, username, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.telephoneNumber, telephoneNumber, this));
        this.dn = "uid="+this.username+",cn=Users,"+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");
    }

    public UserObject(JSONObject jsonObject, LDAPDomain domain)
    {
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            l.log("Adding " + key + " attribute with value "+jsonObject.getString(key));
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.valueOf(key), jsonObject.getString(key), this));
        }
        this.username = this.getAttributeValue(LDAPAttributeEnum.userPrincipalName);
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person", this));
        this.dn = "uid="+this.username+",cn=Users,"+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");

    }

    public UserObject() {

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
