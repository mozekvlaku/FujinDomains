package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import net.vespotok.fujin_domains.directory_service.DomainPool;
import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.credential_provider.JSONTypeEnum;
import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;
import net.vespotok.fujin_domains.login_window.LoginWindowController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class FujinDirectoryAndCredentialProviderServiceApplication {
    private static LDAPDomain domain;
    public static void main(String[] args) throws Exception {
        //

        DirectoryServer directoryServer = new DirectoryServer("127.0.0.1", "admin1");

        LDAPDomain domainToAdd = new LDAPDomain(new LDAPDomainName("uhk.cz", LDAPDomainNameTypeEnum.Win2000Style), "Vespotok", "https://cdn.vespotok.net/img/vespotok_black.svg");

        directoryServer.domainPool.addDomain(domainToAdd);

        domain = directoryServer.domainPool.getDomainByDomainName("uhk.cz");

        LDAPSystemAdministrator administrator = new LDAPSystemAdministrator(domain);
        LDAPUser user = new LDAPUser(domain);

        administrator.authenticate("admin1");


        domain.addObject(new UserObject("Tomáš", "Kracík", "namulnae", "1234", "+420 603 580 970"), administrator);
        user.login("namulnae", "1234");
        domain.loadDefaultGroups(administrator);
        domain.addObject(new UserObject("Karel", "Douda", "doudaka", "1234", "+420 603 580 970"), user);
        domain.addObject(new UserObject("Bořek", "Hnisavý", "hnisabo", "1234", "+420 603 580 970"), user);
        domain.addObject(new UserObject("Polenou", "Kracík", "polekra", "1234", "+420 603 580 970"), user);

        domain.addObject(new GroupObject("sudo", "SuperDoudausers"), user);
        domain.addObject(new GroupObject("Administrators", "Administrators"), user);

        domain.memberOf(domain.searchObjectsByName("Kracík").get(0), domain.searchObjectsByName("Super").get(0), administrator);
        domain.memberOf(domain.searchObjectsByName("Kracík").get(0), domain.searchObjectsByName("Admin").get(0), administrator);

        domain.getObjectByUserPrincipalName("namulnae").addToObject(domain.getObjectByDn("cn=Users,dc=uhk,dc=cz"));

        domain.changeObject(domain.searchObjectsByName("Poleno").get(0), LDAPAttributeEnum.telephoneNumber, "123454545", user);
        domain.changeObject(domain.searchObjectsByName("Douda").get(0), LDAPAttributeEnum.friendlyCountryName, "Meknijsko-Lurk", user);

        LDIFFactory ldifFactory = new LDIFFactory(domain, user);

        String ldifString = "# Testing ldif \n" +
                "dn: uid=doudaka,dc=dc,dc=vespotok,dc=net\n" +
                "changeType: modify\n" +
                "add: description\n" +
                "description: Toto je Kája Doudů";

        //ldifFactory.parseLDIF(ldifString);

        String query = "kracík";
        var results = domain.searchObjectsByName(query);
        for(LDAPObject obj : results)
        {
            System.out.println(obj.toLDIF());
        }

        CredentialProvider credentialProvider = new CredentialProvider(domain);

        credentialProvider.attemptLoginByPrincipalName("namulnae", "12c34");

        /*System.out.println(credentialProvider.getUserAccountJSON(JSONTypeEnum.directoryAttributes));*/
        SpringApplication.run(FujinDirectoryAndCredentialProviderServiceApplication.class, args);

    }

}
