package com.clothes.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ManagerApplication.class);
        app.setAdditionalProfiles("docker");
        app.run(args);
    }
}