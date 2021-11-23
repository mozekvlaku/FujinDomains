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

        domain.addObject(new UserObject("Tomáš", "Kracík", "namulnae", "1234", "+420 603 580 970"), "namulnae");
        domain.addObject(new UserObject("Karel", "Douda", "doudaka", "1234", "+420 603 580 970"), "namulnae");
        domain.addObject(new UserObject("Bořek", "Hnisavý", "hnisabo", "1234", "+420 603 580 970"), "namulnae");
        domain.addObject(new UserObject("Polenou", "Kracík", "polekra", "1234", "+420 603 580 970"), "namulnae");

        domain.addObject(new GroupObject("sudo", "SuperDoudausers"), "namulnae");
        domain.addObject(new GroupObject("Administrators", "Administrators"), "namulnae");

        domain.memberOf(domain.searchObjectsByName("Douda").get(0), domain.searchObjectsByName("Super").get(0), "namulnae");
        domain.memberOf(domain.searchObjectsByName("Douda").get(0), domain.searchObjectsByName("Admin").get(0), "namulnae");

        domain.changeObject(domain.searchObjectsByName("Poleno").get(0), LDAPAttributeEnum.telephoneNumber, "123454545", "namulnae");
        domain.changeObject(domain.searchObjectsByName("Douda").get(0), LDAPAttributeEnum.friendlyCountryName, "Meknijsko-Lurk", "namulnae");

        String query = "Do";
        var results = domain.searchObjectsByName(query);
        for(LDAPObject obj : results)
        {
            System.out.println(obj.toLDIF());
        }

    }

}
