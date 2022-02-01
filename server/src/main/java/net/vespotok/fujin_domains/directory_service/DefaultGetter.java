package net.vespotok.fujin_domains.directory_service;

import net.vespotok.fujin_domains.directory_service.model.*;
import net.vespotok.fujin_domains.directory_service.model.objects.UserObject;

public class DefaultGetter {
    private static LDAPDomain internalDomain;

    public DefaultGetter(DirectoryServer directoryServer) throws Exception {
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println(" __      __       _ _             _                       _           ");
        System.out.println(" \\ \\    / _|     (_|_)           | |                     (_)          ");
        System.out.println("  \\ \\  | |_ _   _ _ _ _ __     __| | ___  _ __ ___   __ _ _ _ __  ___ ");
        System.out.println("   > > |  _| | | | | | '_ \\   / _` |/ _ \\| '_ ` _ \\ / _` | | '_ \\/ __|");
        System.out.println("  / /  | | | |_| | | | | | | | (_| | (_) | | | | | | (_| | | | | \\__ \\");
                System.out.println(" /_/   |_|  \\__,_| |_|_| |_|  \\__,_|\\___/|_| |_| |_|\\__,_|_|_| |_|___/");
        System.out.println("                _/ |                                                  ");
        System.out.println("               |__/                                                   ");
        System.out.println("");
        System.out.println("");
        System.out.println("Vítejte v doménách Fujinu.");
        System.out.println("");
        System.out.println("Toto je první spuštění domén. Do modelu se načte interní doména, do které se přihlásíte");
        System.out.println("a založíte první doménu.");
        System.out.println("");
        System.out.println("Aplikace nyní doménu nahraje, uloží do databáze a poté se ukončí. Při dalším spuštění");
        System.out.println("se již načte vše z databáze a systém je připraven.");
        System.out.println("");
        System.out.println("Pokračujte stiskem tlačítka ENTER");
        System.in.read();

        internalDomain = new LDAPDomain(new LDAPDomainName("BUILTIN", LDAPDomainNameTypeEnum.NT4Style),"Systém Fujin Domains", "http://cdn.vespotok.net/img/fujindomainshorizontal.svg");
        internalDomain.setEm(null);
        LDAPSystemAdministrator administrator = new LDAPSystemAdministrator(internalDomain);
        administrator.authenticate(directoryServer.getAdministratorPassword());
        internalDomain.loadDefaultGroups(administrator);
        internalDomain.addObject(new UserObject("Systémový", "administrátor", "Administrator", directoryServer.getAdministratorPassword(), "",internalDomain), administrator);
        internalDomain.memberOf(internalDomain.getObjectByUserPrincipalName("Administrator"), internalDomain.getObjectByDn("cn=Server Admins,cn=Users," + internalDomain.getDomainName().toDN()), administrator);

        directoryServer.domainPool.addDomain(internalDomain);
        System.out.println("");
        System.out.println("Doména načtena a uložena.");
        System.out.println("Ukončuji se.");
        System.exit(0);
    }
}
