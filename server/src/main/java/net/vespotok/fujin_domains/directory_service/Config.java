package net.vespotok.fujin_domains.directory_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("net.vespotok.fujin_domains.directory_service") // the base package you want to scan
public class Config {

    @Bean
    public DirectoryServer getThisServer() throws Exception {
        return new DirectoryServer("auth.vespotok.com", "admin1");
    }
}