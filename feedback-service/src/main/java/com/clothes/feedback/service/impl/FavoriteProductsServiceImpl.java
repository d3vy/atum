package com.clothes.feedback.service.impl;

import com.clothes.feedback.model.FavoriteProduct;
import com.clothes.feedback.repository.FavoriteProductsRepository;
import com.clothes.feedback.service.general.FavoriteProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class FavoriteProductsServiceImpl implements FavoriteProductsService {

    private final FavoriteProductsRepository favoriteProductsRepository;

    public FavoriteProductsServiceImpl(FavoriteProductsRepository favoriteProductsRepository) {
        this.favoriteProductsRepository = favoriteProductsRepository;
    }

    @Override
    public Mono<FavoriteProduct> addProductToFavorites(Integer productId, String userId) {
        return this.favoriteProductsRepository.save(
                new FavoriteProduct(UUID.randomUUID(), productId, userId)
        );
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Integer productId, String userId) {
        return this.favoriteProductsRepository.removeByProductIdAndUserId(productId, userId);
    }

    @Override
    public Mono<FavoriteProduct> findFavoriteProductByProductId(Integer productId, String userId) {
        return this.favoriteProductsRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavoriteProduct> findAllFavoriteProducts(String userId) {
        return this.favoriteProductsRepository.findAllByUserId(userId);
    }
}
