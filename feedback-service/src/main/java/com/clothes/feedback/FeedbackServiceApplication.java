package com.clothes.feedback;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${keycloak.uri}/realms/atum/protocol/openid-connect/auth",
                        tokenUrl = "${keycloak.uri}/realms/atum/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid"),
                                @OAuthScope(name = "microprofile-jwt")
                        }
                )
        )
)
public class FeedbackServiceApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FeedbackServiceApplication.class);
        app.setAdditionalProfiles("standalone");
        app.run(args);
    }
}