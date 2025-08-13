package com.clothes.manager.config;

import com.clothes.manager.client.impl.CategoriesClientImpl;
import com.clothes.manager.client.impl.CategoryClientImpl;
import com.clothes.manager.client.impl.ProductClientImpl;
import com.clothes.manager.client.impl.ProductsClientImpl;
import com.clothes.manager.security.OAuthClientHttpRequestInterceptor;
import de.codecentric.boot.admin.client.registration.BlockingRegistrationClient;
import de.codecentric.boot.admin.client.registration.RegistrationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;


@Configuration
public class ClientBeans {


    @Bean
    public ProductsClientImpl productsClient(
            @Value("${catalogue.service.base.uri:http://catalogue-service:8080}") String catalogueBaseUri,
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
            @Value("${catalogue.service.base.uri:http://catalogue-service:8080}") String catalogueBaseUri,
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

    @Bean
    public CategoriesClientImpl categoriesClient(
            @Value("${catalogue.service.base.uri:http://catalogue-service:8080}") String catalogueBaseUri,
            @Value("${catalogue.service.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        return new CategoriesClientImpl(RestClient.builder()
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
    public CategoryClientImpl categoryClient(
            @Value("${catalogue.service.base.uri:http://catalogue-service:8080}") String catalogueBaseUri,
            @Value("${catalogue.service.registration-id:keycloak}") String registrationId,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository
    ) {
        return new CategoryClientImpl(RestClient.builder()
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
    @ConditionalOnProperty(name = "spring.boot.admin.client.enabled", havingValue = "true")
    public RegistrationClient registrationClient(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService
                );

        RestTemplate restTemplate = new RestTemplateBuilder()
                .interceptors((request, body, execution) -> {
                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(OAuth2AuthorizeRequest
                                .withClientRegistrationId("metrics")
                                .principal("manager-application-metrics-client")
                                .build());


                        assert authorizedClient != null;
                        String token = authorizedClient.getAccessToken().getTokenValue();
                        request.getHeaders().setBearerAuth(token);
                    }

                    return execution.execute(request, body);
                })
                .build();

        return new BlockingRegistrationClient(restTemplate);
    }

}
