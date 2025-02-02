package com.clothes.customer.client.impl;

import com.clothes.customer.client.general.ProductReviewsClient;
import com.clothes.customer.client.payload.NewProductReviewPayload;
import com.clothes.customer.model.ProductReview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class ProductReviewsClientImpl implements ProductReviewsClient {

    private final WebClient webClient;

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Integer productId) {
        return this.webClient
                .get()
                .uri("/api/v1/product-reviews/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToFlux(ProductReview.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(Integer productId, Integer rating, String review) {
        return this.webClient
                .post()
                .uri("/api/v1/product-reviews")
                .bodyValue(new NewProductReviewPayload(productId, rating, review))
                .retrieve()
                .bodyToMono(ProductReview.class);

    }
}
