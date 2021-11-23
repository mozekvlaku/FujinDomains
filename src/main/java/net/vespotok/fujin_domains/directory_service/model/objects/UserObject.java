package net.vespotok.fujin_domains.directory_service.model.objects;

import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttribute;
import net.vespotok.fujin_domains.directory_service.model.LDAPAttributeEnum;
import net.vespotok.fujin_domains.directory_service.model.LDAPObject;

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
    }

    public void setDomainName(LDAPDomainName domainName)
    {
        this.domainName = domainName;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, "uid="+this.username+","+this.domainName.toDN()));
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
