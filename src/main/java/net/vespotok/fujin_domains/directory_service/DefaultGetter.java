package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.credential_provider.CredentialProvider;
import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.GroupObject;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

public class DefaultGetter {
    private static LDAPDomain domain;
    private static LDAPDomain domain2;
    public DefaultGetter(DirectoryServer directoryServer) throws Exception {
        LDAPDomain domainToAdd1 = new LDAPDomain(new LDAPDomainName("dc.vespotok.net", LDAPDomainNameTypeEnum.Win2000Style), "Vespotok", "https://cdn.vespotok.net/img/vespotok_black.svg");
        LDAPDomain domainToAdd2 = new LDAPDomain(new LDAPDomainName("uhk.cz", LDAPDomainNameTypeEnum.Win2000Style), "UHK", "https://www.uhk.cz/img/svg/logo/uhk-uhk-cs_hor.svg");
        LDAPDomain domainToAdd3 = new LDAPDomain(new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style), "", "");
        directoryServer.domainPool.addDomain(domainToAdd1);
        directoryServer.domainPool.addDomain(domainToAdd2);
        directoryServer.domainPool.addDomain(domainToAdd3);

        domain = directoryServer.domainPool.getDomainByDomainName("uhk.cz");
        domain2 = directoryServer.domainPool.getDomainByDomainName("dc.vespotok.net");

        LDAPSystemAdministrator administrator = new LDAPSystemAdministrator(domain);
        LDAPUser user = new LDAPUser(domain);

        administrator.authenticate("admin1");
        LDAPSystemAdministrator administrator2 = new LDAPSystemAdministrator(domain2);
        LDAPUser user2 = new LDAPUser(domain2);

        administrator2.authenticate("admin1");


        domain.addObject(new UserObject("Tomáš", "Kracík", "namulnae", "1234", "+420 603 580 970"), administrator);
        user.login("namulnae", "1234");
        domain.loadDefaultGroups(administrator);
        domain.addObject(new UserObject("Karel", "Douda", "doudaka", "1234", "+420 603 580 970"), user);
        domain.addObject(new UserObject("Bořek", "Hnisavý", "hnisabo", "1234", "+420 603 580 970"), user);
        domain2.addObject(new UserObject("Bořek", "Hnisavý", "hnisabo", "1234", "+420 603 580 970"), administrator2);
        domain.addObject(new UserObject("Polenou", "Kracík", "polekra", "1234", "+420 603 580 970"), user);

        domain.addObject(new GroupObject("sudo", "SuperDoudausers"), user);
        domain.addObject(new GroupObject("Administrators", "Administrators"), user);

        domain.memberOf(domain.searchObjectsByName("Kracík").get(0), domain.searchObjectsByName("Super").get(0), administrator);
        domain.memberOf(domain.searchObjectsByName("Kracík").get(0), domain.searchObjectsByName("Admin").get(0), administrator);
        String query = "Douda";
        var results = domain.searchObjectsByName(query);
        for(LDAPObject obj : results)
        {
            System.out.println(obj.toLDIF());
        }
        domain.getObjectByUserPrincipalName("namulnae").addToObject(domain.getObjectByDn("cn=Users,dc=uhk,dc=cz"));
        domain.getObjectByUserPrincipalName("doudaka").addToObject(domain.getObjectByDn("cn=Users,dc=uhk,dc=cz"));

        domain.changeObject(domain.searchObjectsByName("Poleno").get(0), LDAPAttributeEnum.telephoneNumber, "123454545", user);
        domain.changeObject(domain.searchObjectsByName("Douda").get(0), LDAPAttributeEnum.friendlyCountryName, "Meknijsko-Lurk", user);

        LDIFFactory ldifFactory = new LDIFFactory(domain, user);

        String ldifString = "# Testing ldif \n" +
                "dn: uid=doudaka,dc=dc,dc=vespotok,dc=net\n" +
                "changeType: modify\n" +
                "add: description\n" +
                "description: Toto je Kája Doudů";

        //ldifFactory.parseLDIF(ldifString);

        /*

        CredentialProvider credentialProvider = new CredentialProvider(domain);

        credentialProvider.attemptLoginByPrincipalName("namulnae", "12c34");*/
    }
}
