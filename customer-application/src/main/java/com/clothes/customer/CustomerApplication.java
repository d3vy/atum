package com.clothes.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CustomerApplication.class);
        app.setAdditionalProfiles("standalone");
        app.run(args);
    }
}