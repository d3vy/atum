package com.clothes.catalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogueServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CatalogueServiceApplication.class);
        app.setAdditionalProfiles("standalone");
        app.run(args);
    }
}