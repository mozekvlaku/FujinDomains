package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;

import javax.persistence.Entity;

@Entity
public class OrganizationalUnitObject extends LDAPObject {
    private String name;

    public OrganizationalUnitObject(String name, LDAPDomain domain)
    {
        this.name = name;
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "organizationalUnit", this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory OU");
        this.dn = "ou="+this.name+","+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
    }

    public OrganizationalUnitObject() {

    }
}
