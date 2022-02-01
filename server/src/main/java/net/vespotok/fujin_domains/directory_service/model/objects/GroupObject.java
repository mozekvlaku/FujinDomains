package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;

import javax.persistence.Entity;
import java.util.Iterator;
import java.util.UUID;

@Entity
public class GroupObject extends LDAPObject {
    private String name;

    public GroupObject(String name, String friendlyName, LDAPDomain domain)
    {
        this.name = name;
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "group", this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.name, friendlyName, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name, this));
        this.dn = "cn="+this.name+","+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory Group");
    }

    public GroupObject() {

    }

    public GroupObject(JSONObject jsonObject, LDAPDomain domain) {
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            l.log("Adding " + key + " attribute with value "+jsonObject.getString(key));
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.valueOf(key), jsonObject.getString(key), this));
        }
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "group", this));
        this.dn = "cn="+this.getAttributeValue(LDAPAttributeEnum.cn)+",cn=Users,"+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");
        generateFjid();
    }
    private void generateFjid()
    {
        var start = "fjid";
        var prefix = "-fj9-";
        var unique = UUID.randomUUID().toString();
        var end = "g";
        if(this.getAttributeValue(LDAPAttributeEnum.fujinId) == "" ||this.getAttributeValue(LDAPAttributeEnum.fujinId)==null)
        {
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.fujinId, (start + prefix + unique + end), this));
        }
    }
}
