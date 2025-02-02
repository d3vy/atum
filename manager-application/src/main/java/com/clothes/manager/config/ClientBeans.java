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

/**
 * Конфигурационный класс для создания бинов клиентов продуктов.
 */
@Configuration
public class ClientBeans {

    /**
     * Создает бин ProductsClientImpl для взаимодействия с сервисом каталога продуктов.
     *
     * @param catalogueBaseUri                Базовый URI сервиса каталога продуктов.
     * @param registrationId                  Идентификатор регистрации клиента OAuth2.
     * @param clientRegistrationRepository    Репозиторий регистраций клиентов.
     * @param auth2AuthorizedClientRepository Репозиторий авторизованных клиентов OAuth2.
     * @return Экземпляр ProductsClientImpl.
     */
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

    /**
     * Создает бин ProductClientImpl для взаимодействия с сервисом каталога продуктов.
     *
     * @param catalogueBaseUri                Базовый URI сервиса каталога продуктов.
     * @param registrationId                  Идентификатор регистрации клиента OAuth2.
     * @param clientRegistrationRepository    Репозиторий регистраций клиентов.
     * @param auth2AuthorizedClientRepository Репозиторий авторизованных клиентов OAuth2.
     * @return Экземпляр ProductClientImpl.
     */
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

}
