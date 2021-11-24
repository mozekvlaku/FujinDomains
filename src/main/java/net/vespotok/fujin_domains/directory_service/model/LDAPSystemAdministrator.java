package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

import java.util.Objects;

public class LDAPSystemAdministrator extends LDAPUser {
    private String administratorPassword = "admin1";

    public LDAPSystemAdministrator(LDAPDomain loginDomain) {
        super(loginDomain);
    }

    public final boolean authenticate(String password)
    {
        this.userObject = new UserObject("Administrator", "", "Administrator", administratorPassword, "", this.loginDomain.getDomainName());
        return Objects.equals(password, administratorPassword);
    }
}
