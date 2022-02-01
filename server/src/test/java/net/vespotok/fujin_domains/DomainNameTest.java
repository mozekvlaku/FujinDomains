package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.model.LDAPDomainName;
import net.vespotok.fujin_domains.directory_service.model.LDAPDomainNameTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DomainNameTest {

    @Test
    void testFromNT4()
    {
        LDAPDomainName testDomainName = new LDAPDomainName("UHK", LDAPDomainNameTypeEnum.NT4Style);
        System.out.println("DN: "+testDomainName.toDN());
        System.out.println("NT4: "+testDomainName.toNT4Style());
        System.out.println("2000: "+testDomainName.toWin2000Style());
    }

    @Test
    void testFromNT5()
    {
        LDAPDomainName testDomainName = new LDAPDomainName("domain.uhk.cz", LDAPDomainNameTypeEnum.Win2000Style);
        System.out.println("DN: "+testDomainName.toDN());
        System.out.println("NT4: "+testDomainName.toNT4Style());
        System.out.println("2000: "+testDomainName.toWin2000Style());
    }

}
