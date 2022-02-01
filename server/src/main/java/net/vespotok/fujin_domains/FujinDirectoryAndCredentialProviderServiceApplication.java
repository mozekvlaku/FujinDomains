package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.Config;
import net.vespotok.fujin_domains.directory_service.DefaultGetter;
import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@SpringBootApplication
public class FujinDirectoryAndCredentialProviderServiceApplication {

    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FujinDirectoryAndCredentialProviderServiceApplication.class, args);
    }

}
