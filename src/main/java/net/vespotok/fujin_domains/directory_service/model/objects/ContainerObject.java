package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPObject;

public class ContainerObject extends LDAPObject {
    private String name;

    public ContainerObject(String name)
    {
        this.name = name;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "container"));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name));
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, "cn="+this.name+","+this.domainName.toDN()));
    }
}
