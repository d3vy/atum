package com.clothes.feedback.service.general;

import com.clothes.feedback.model.FavoriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavoriteProductsService {

    Mono<FavoriteProduct> addProductToFavorites(Integer productId, String userId);

    Mono<Void> removeProductFromFavorites(Integer productId, String userId);

    Mono<FavoriteProduct> findFavoriteProductByProductId(Integer productId, String userId);

    Flux<FavoriteProduct> findAllFavoriteProducts(String userId);
}
