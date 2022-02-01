package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DomainTest {
    @Test
    void ComplexDomainTest() throws Exception {
        LDAPDomain domain = new LDAPDomain(new LDAPDomainName("UHK", LDAPDomainNameTypeEnum.NT4Style));
        domain.setEm(null);
        LDAPSystemAdministrator administrator = new LDAPSystemAdministrator(domain);
        administrator.authenticate("admin1");
        domain.loadDefaultGroups(administrator);
        domain.addObject(new UserObject("Systémový", "administrátor", "Administrator", "admin1", "",domain), administrator);
        domain.memberOf(domain.getObjectByUserPrincipalName("Administrator"), domain.getObjectByDn("cn=Server Admins,cn=Users," + domain.getDomainName().toDN()), administrator);
        for(LDAPObject object : domain.getDomainObjects())
        {
            System.out.println("LDIF:");
            System.out.println(object.toLDIF());
            System.out.println("JSON:");
            System.out.println(object.toJSON());
        }
    }
}
