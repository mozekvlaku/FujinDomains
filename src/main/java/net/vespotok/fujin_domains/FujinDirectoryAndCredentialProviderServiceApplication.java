package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FujinDirectoryAndCredentialProviderServiceApplication {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(FujinDirectoryAndCredentialProviderServiceApplication.class, args);

        LDAPDomain domain = new LDAPDomain(new LDAPDomainName("dc.vespotok.net", LDAPDomainNameTypeEnum.Win2000Style), "Vespotok", "https://cdn.vespotok.net/img/vespotok_black.svg");

        LDAPSystemAdministrator administrator = new LDAPSystemAdministrator(domain);
        LDAPUser user = new LDAPUser(domain);

        administrator.authenticate("admin1");

        domain.addObject(new UserObject("Tomáš", "Kracík", "namulnae", "1234", "+420 603 580 970"), administrator);
        user.login("namulnae", "1234");

        domain.addObject(new UserObject("Karel", "Douda", "doudaka", "1234", "+420 603 580 970"), user);
        domain.addObject(new UserObject("Bořek", "Hnisavý", "hnisabo", "1234", "+420 603 580 970"), user);
        domain.addObject(new UserObject("Polenou", "Kracík", "polekra", "1234", "+420 603 580 970"), user);

        domain.addObject(new GroupObject("sudo", "SuperDoudausers"), user);
        domain.addObject(new GroupObject("Administrators", "Administrators"), user);

        domain.memberOf(domain.searchObjectsByName("Douda").get(0), domain.searchObjectsByName("Super").get(0), user);
        domain.memberOf(domain.searchObjectsByName("Douda").get(0), domain.searchObjectsByName("Admin").get(0), user);

        domain.changeObject(domain.searchObjectsByName("Poleno").get(0), LDAPAttributeEnum.telephoneNumber, "123454545", user);
        domain.changeObject(domain.searchObjectsByName("Douda").get(0), LDAPAttributeEnum.friendlyCountryName, "Meknijsko-Lurk", user);

        LDIFFactory ldifFactory = new LDIFFactory(domain, user);

        String ldifString = "# Testing ldif \n" +
                "dn: uid=doudaka,dc=dc,dc=vespotok,dc=net\n" +
                "changeType: modify\n" +
                "add: description\n" +
                "description: Toto je Kája Doudů";

        ldifFactory.parseLDIF(ldifString);

        String query = "doud";
        var results = domain.searchObjectsByName(query);
        for(LDAPObject obj : results)
        {
            System.out.println(obj.toLDIF());
        }

    }

}
