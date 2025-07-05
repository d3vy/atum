package com.atum.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ConfigServerApplication.class);
        application.setAdditionalProfiles("standalone", "native");
        application.run(args);
    }

}
