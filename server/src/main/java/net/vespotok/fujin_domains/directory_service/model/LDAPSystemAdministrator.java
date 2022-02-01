package net.vespotok.fujin_domains.directory_service.model;

import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

public class LDAPSystemAdministrator extends LDAPUser {
    private String administratorPassword = "admin1";

    public LDAPSystemAdministrator(LDAPDomain loginDomain) {
        super(loginDomain);
    }

    public final boolean authenticate(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.userObject = new UserObject("Administrator", "", "Administrator", administratorPassword, "", this.loginDomain);
        return userObject.checkPassword(password);
    }

    public String getUsername()
    {
        return "Administrator";
    }
}
