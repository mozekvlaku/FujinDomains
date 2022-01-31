package net.vespotok.fujin_domains.directory_service.helpers;

import net.vespotok.fujin_domains.directory_service.model.*;

import java.util.Arrays;
import java.util.Objects;

public class AccessRightsHelper {
    private LDAPDomain checkingDomain;
    private Logging l;

    public AccessRightsHelper(LDAPDomain checkingDomain) {
        this.checkingDomain = checkingDomain;
        this.l = new Logging(LoggingLevel.print, checkingDomain.getDomainName(), "ACCESSRIGHTS");
    }

    public boolean hasRightsToObject(LDAPObject ldapObject, LDAPUser ldapUser, AccessRightLevelEnum accessRightLevel, LDAPAccessRightEnum accessRight) {
        l.log("Checking " + ldapUser.getUsername() + " for access right " + accessRight.name() + " at " + accessRightLevel.name() + " level to object " + ldapObject.getDn());
        if (ldapUser instanceof LDAPSystemAdministrator)
        {
            l.log("User is an internal administrator.");
            return true;
        }
        switch (accessRightLevel) {
            case BASIC:
                return this.checkBasicRights(ldapUser, ldapObject, accessRight);

            case PERSONAL:
                return this.checkPersonalRights(ldapUser, ldapObject);

            case DOMAIN:
                return this.checkDomainRights(ldapUser);

            case ENTERPRISE:
                return this.checkEnterpriseRights(ldapUser);

            case SERVER:
                return this.checkServerRights(ldapUser);
        }
        return false;
    }

    public boolean hasRightsToDomain(LDAPUser ldapUser) {
        l.log("Checking " + ldapUser.getUsername() + " for access right for domain with ENTERPRISE level to object");
        if (ldapUser instanceof LDAPSystemAdministrator)
        {
            l.log("User is an internal administrator.");
            return true;
        }
        return this.checkEnterpriseRights(ldapUser);
    }

    public boolean hasRightsToAdd(LDAPUser ldapUser) {
        l.log("Checking " + ldapUser.getUsername() + " for access right to add an object with DOMAIN level to object");
        if (ldapUser instanceof LDAPSystemAdministrator)
        {
            l.log("User is an internal administrator.");
            return true;
        }
        return this.checkDomainRights(ldapUser);
    }


    private boolean checkBasicRights(LDAPUser user, LDAPObject object, LDAPAccessRightEnum accessRight)
    {
        l.log("Checking basic rights.");

        if(object.hasRightsTo(user, accessRight))
        {
            return true;
        }
        l.log("Probably no basic rights.");

        return this.checkPersonalRights(user, object);
    }

    private boolean checkPersonalRights(LDAPUser user, LDAPObject object)
    {
        l.log("Checking personal rights.");

        if(Objects.equals(object.getSID(), user.getSID()))
        {
            return true;
        }
        l.log("Probably no personal rights.");
        return checkDomainRights(user);
    }
    private boolean checkServerRights(LDAPUser user)
    {
        l.log("Checking server rights.");

        LDAPObject serverGroup  = this.checkingDomain.getObjectByDn("cn=Server Admins,cn=Users," + this.checkingDomain.getDomainName().toDN());
        String checkingUserSID = user.getSID();
        String[] serverGroupMembers = serverGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");

        if(Arrays.asList(serverGroupMembers).contains(checkingUserSID))
        {
            l.success("Got Server Admin");
            return true;
        }
        else
        {
            l.error("No rights.");
            return false;
        }
    }
    private boolean checkEnterpriseRights(LDAPUser user)
    {
        l.log("Checking enterprise rights.");

        if(checkServerRights(user))
        {
            return true;
        }
        l.log("Probably no server rights.");
        LDAPObject enterpriseGroup  = this.checkingDomain.getObjectByDn("cn=Enterprise Admins,cn=Users," + this.checkingDomain.getDomainName().toDN());
        String checkingUserSID = user.getSID();
        String[] serverGroupMembers = enterpriseGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");

        return Arrays.asList(serverGroupMembers).contains(checkingUserSID);
    }
    private boolean checkDomainRights(LDAPUser user)
    {
        l.log("Checking domain rights.");

        if(checkEnterpriseRights(user))
        {
            return true;
        }
        LDAPObject domainGroup  = this.checkingDomain.getObjectByDn("cn=Domain Admins,cn=Users," + this.checkingDomain.getDomainName().toDN());
        String checkingUserSID = user.getSID();
        String[] serverGroupMembers = domainGroup.getAttributeValue(LDAPAttributeEnum.member).split(",");

        return Arrays.asList(serverGroupMembers).contains(checkingUserSID);
    }
}
