package com.clothes.customer.client.general;

import com.clothes.customer.model.FavoriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavoriteProductsClient {

    Flux<FavoriteProduct> findAllFavoriteProducts();

    Mono<FavoriteProduct> findFavoriteProductByProductId(Integer productId);

    Mono<FavoriteProduct> addProductToFavorites(Integer productId);

    Mono<Void> removeProductFromFavorites(Integer productId);
}
