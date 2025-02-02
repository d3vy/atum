package com.clothes.customer.config;

import com.clothes.customer.client.impl.FavoriteProductsClientImpl;
import com.clothes.customer.client.impl.ProductReviewsClientImpl;
import com.clothes.customer.client.impl.ProductsClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientBeans {

    @Bean
    public WebClient.Builder atumServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrationRepository,
                        authorizedClientRepository
                );
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .filter(filter);
    }

    @Bean
    public ProductsClientImpl productsClientImpl(
            @Value("${atum.services.catalogue.uri:http://localhost:8080}") String catalogueBaseUri,
            WebClient.Builder atumServicesWebClientBuilder
    ) {
        return new ProductsClientImpl(
                atumServicesWebClientBuilder
                        .baseUrl(catalogueBaseUri)
                        .build()
        );
    }

    @Bean
    public FavoriteProductsClientImpl favoriteProductsClientImpl(
            @Value("${atum.services.feedback.uri:http://localhost:8083}") String feedbackBaseUri,
            WebClient.Builder atumServicesWebClientBuilder
    ) {
        return new FavoriteProductsClientImpl(
                atumServicesWebClientBuilder
                        .baseUrl(feedbackBaseUri)
                        .build()
        );
    }

    @Bean
    public ProductReviewsClientImpl productReviewsClientImpl(
            @Value("${atum.services.reviews.uri:http://localhost:8083}") String reviewsBaseUri,
            WebClient.Builder atumServicesWebClientBuilder
    ) {
        return new ProductReviewsClientImpl(
                atumServicesWebClientBuilder
                        .baseUrl(reviewsBaseUri)
                        .build()
        );
    }
}
