package com.clothes.customer.client.impl;

import com.clothes.customer.client.general.FavoriteProductsClient;
import com.clothes.customer.client.payload.NewFavoriteProductPayload;
import com.clothes.customer.model.FavoriteProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class FavoriteProductsClientImpl implements FavoriteProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<FavoriteProduct> findAllFavoriteProducts() {
        return this.webClient
                .get()
                .uri("/api/v1/favorite-products")
                .retrieve().bodyToFlux(FavoriteProduct.class);
    }

    @Override
    public Mono<FavoriteProduct> findFavoriteProductByProductId(Integer productId) {
        return this.webClient
                .get()
                .uri("/api/v1/favorite-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavoriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavoriteProduct> addProductToFavorites(Integer productId) {
        return this.webClient
                .post()
                .uri("/api/v1/favorite-products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new NewFavoriteProductPayload(productId))
                .retrieve()
                .bodyToMono(FavoriteProduct.class);
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Integer productId) {
        return this.webClient
                .delete()
                .uri("/api/v1/favorite-products/by-product-id/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
