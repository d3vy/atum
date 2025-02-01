package com.clothes.feedback.repository;

import com.clothes.feedback.model.FavoriteProduct;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FavoriteProductsRepository extends ReactiveCrudRepository<FavoriteProduct, UUID> {

    Flux<FavoriteProduct> findAllByUserId(String userId);

    Mono<FavoriteProduct> findByProductIdAndUserId(Integer productId, String userId);

    Mono<Void> removeByProductIdAndUserId(Integer productId, String userId);

}
