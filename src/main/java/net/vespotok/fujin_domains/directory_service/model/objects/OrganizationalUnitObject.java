package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPObject;

public class OrganizationalUnitObject extends LDAPObject {
    private String name;

    public OrganizationalUnitObject(String name)
    {
        this.name = name;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "organizationalUnit"));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name));
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, "ou="+this.name+","+this.domainName.toDN()));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory OU");

    }
}
