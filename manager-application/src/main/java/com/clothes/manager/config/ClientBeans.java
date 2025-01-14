package com.clothes.manager.config;

import com.clothes.manager.client.impl.ProductClientImpl;
import com.clothes.manager.client.impl.ProductsClientImpl;
import com.clothes.manager.security.OAuthClientHttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public ProductsClientImpl productsClient(
            @Value("${catalogue.service.base.uri:http://localhost:8080}") String catalogueBaseUri,
            @Value("${catalogue.service.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        return new ProductsClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(
                        new OAuthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                        auth2AuthorizedClientRepository), registrationId
                        )
                )
                .build());
    }

    @Bean
    public ProductClientImpl productClient(
            @Value("${catalogue.service.base.uri:http://localhost:8080}") String catalogueBaseUri,
            @Value("${catalogue.service.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        return new ProductClientImpl(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(
                        new OAuthClientHttpRequestInterceptor(
                                new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                        auth2AuthorizedClientRepository), registrationId
                        )
                )
                .build());
    }
}
