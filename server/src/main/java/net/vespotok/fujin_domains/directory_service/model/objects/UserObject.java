package net.vespotok.fujin_domains.directory_service.model.objects;

import at.favre.lib.crypto.bcrypt.BCrypt;
import net.vespotok.fujin_domains.directory_service.helpers.Logging;
import net.vespotok.fujin_domains.directory_service.helpers.LoggingLevel;
import net.vespotok.fujin_domains.directory_service.model.*;
import org.json.JSONObject;

import javax.persistence.Entity;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Entity
public class UserObject extends LDAPObject {

    private String username;

    public UserObject(String name, String surname, String username, String password, String telephoneNumber, LDAPDomain domain) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.username = username;
        generateFjid();
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person", this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.cn, name + " " + surname, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.givenName, name, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.sn, surname, this));
        setPassword(password);
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPrincipalName, username, this));
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.telephoneNumber, telephoneNumber, this));
        this.dn = "uid="+this.username+",cn=Users,"+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");

    }

    public UserObject(JSONObject jsonObject, LDAPDomain domain) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.domainName = domain.getDomainName();
        this.ldapDomain = domain;
        Iterator<String> keys = jsonObject.keys();
        generateFjid();
        while (keys.hasNext()) {
            String key = keys.next();
            if(Objects.equals(LDAPAttributeEnum.valueOf(key), LDAPAttributeEnum.userPassword))
            {
                setPassword(jsonObject.getString(key));
            }
            else
            {
                l.log("Adding " + key + " attribute with value "+jsonObject.getString(key));
                this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.valueOf(key), jsonObject.getString(key), this));
            }
        }
        this.username = this.getAttributeValue(LDAPAttributeEnum.userPrincipalName);
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.objectClass, "person", this));
        this.dn = "uid="+this.username+",cn=Users,"+this.domainName.toDN();
        this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.dn, this.dn, this));
        this.l = new Logging(LoggingLevel.print, domainName, "Directory User");


    }

    public UserObject() {

    }

    private void generateFjid()
    {
        var start = "fjid";
        var prefix = "-fj9-";
        var unique = UUID.randomUUID().toString();
        var end = "u";
        if(this.getAttributeValue(LDAPAttributeEnum.fujinId) == "" ||this.getAttributeValue(LDAPAttributeEnum.fujinId)==null)
        {
            l.log("Generating new FJID " + (start + prefix + unique + end));
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.fujinId, (start + prefix + unique + end), this));
        }
        else
        {
            l.log("FJID already present " + this.getAttributeValue(LDAPAttributeEnum.fujinId));
        }
    }

    public String getUserLevel()
    {
        LDAPObject serverGroup  = this.ldapDomain.getObjectByDn("cn=Server Admins,cn=Users," + ldapDomain.getDomainName().toDN());
        String[] serverGroupMembers = new String[0];
        try {
            serverGroupMembers = serverGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");
        }
        catch(Exception e)
        {}
        if(Arrays.asList(serverGroupMembers).contains(this.getSID()))
        {
            return "SERVER";
        }
        else
        {
            LDAPObject enterpriseGroup  = this.ldapDomain.getObjectByDn("cn=Enterprise Admins,cn=Users," + ldapDomain.getDomainName().toDN());
            String[] enterpriseGroupGroupMembers = new String[0];
            try {
                enterpriseGroupGroupMembers = enterpriseGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");
            }
            catch(Exception e)
            {}
            if(Arrays.asList(enterpriseGroupGroupMembers).contains(this.getSID()))
            {
                return "ENTERPRISE";
            }
            else
            {
                LDAPObject domainGroup  = this.ldapDomain.getObjectByDn("cn=Domain Admins,cn=Users," + ldapDomain.getDomainName().toDN());
                String[] domainGroupGroupMembers = new String[0];
                try {
                    domainGroupGroupMembers = domainGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");
                }
                catch(Exception e)
                {}
                if(Arrays.asList(domainGroupGroupMembers).contains(this.getSID()))
                {
                    return "DOMAIN";
                }
                else
                {
                    return "USER";
                }
            }
        }
    }

    public boolean checkPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.getAttributeValue(LDAPAttributeEnum.userPassword));

        if(result.verified)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void setPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        password = this.encryptPassword(password);
        if(this.getAttributeValue(LDAPAttributeEnum.userPassword) == null)
        {
            this.addAttribute(new LDAPAttribute(LDAPAttributeEnum.userPassword, password, this));
        }
        else
        {
            this.changeAttribute(LDAPAttributeEnum.userPassword, password);
        }
    }

    private String encryptPassword(String password) {
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return bcryptHashString;
    }

    public boolean changePassword(String oldPassword, String new1, String new2) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(checkPassword(oldPassword))
        {
            if(Objects.equals(new1, new2))
            {
                this.setPassword(new1);
                return true;
            }
            else
            {
                return false;
            }
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
