package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;

import javax.persistence.Entity;

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

}
