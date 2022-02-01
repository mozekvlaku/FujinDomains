package net.vespotok.fujin_domains.directory_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@ComponentScan("net.vespotok.fujin_domains.directory_service") // the base package you want to scan
public class Config {

    private static DirectoryServer thisServer;

    static {
        try {
            thisServer = new DirectoryServer("auth.vespotok.com", "admin1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public DirectoryServer getThisServer() {
        return this.thisServer;
    }
}