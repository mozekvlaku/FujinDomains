package net.vespotok.fujin_domains;

import net.vespotok.fujin_domains.directory_service.Config;
import net.vespotok.fujin_domains.directory_service.DefaultGetter;
import net.vespotok.fujin_domains.directory_service.DirectoryServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class FujinDirectoryAndCredentialProviderServiceApplication {

    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FujinDirectoryAndCredentialProviderServiceApplication.class, args);
        FujinDirectoryAndCredentialProviderServiceApplication fjdcs = new FujinDirectoryAndCredentialProviderServiceApplication();
        fjdcs.defaults();
    }

    public void defaults() throws Exception {
        DirectoryServer server = context.getBean("getThisServer", DirectoryServer.class);
        DefaultGetter df = new DefaultGetter(server);
    }

}
