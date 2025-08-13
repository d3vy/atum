package com.clothes.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAdminServer
public class AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AdminServerApplication.class);
        app.setAdditionalProfiles("docker");
        app.run(args);
    }
}