package com.clothes.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedbackServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FeedbackServiceApplication.class);
        app.setAdditionalProfiles("standalone");
        app.run(args);
    }
}