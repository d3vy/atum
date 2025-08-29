package com.clothes.feedback.controller;

import com.clothes.feedback.controller.payload.NewFavoriteProductPayload;
import com.clothes.feedback.model.FavoriteProduct;
import com.clothes.feedback.service.general.FavoriteProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api/v1/favorite-products")
public class FavoriteProductsController {

    private final FavoriteProductsService favoriteProductsService;

    public FavoriteProductsController(FavoriteProductsService favoriteProductsService) {
        this.favoriteProductsService = favoriteProductsService;
    }

    @GetMapping
    public Flux<FavoriteProduct> findFavoriteAllFavoriteProducts(
            Mono<JwtAuthenticationToken> jwtAuthenticationTokenMono
    ) {
        return jwtAuthenticationTokenMono.flatMapMany(token ->
                this.favoriteProductsService
                        .findAllFavoriteProducts(token.getToken().getSubject()));
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavoriteProduct> findFavoriteProductByProductId(
            Mono<JwtAuthenticationToken> jwtAuthenticationTokenMono,
            @PathVariable("productId") Integer productId
    ) {
        return jwtAuthenticationTokenMono.flatMap(token ->
                this.favoriteProductsService
                        .findFavoriteProductByProductId(productId, token.getToken().getSubject()));
    }

    @PostMapping
    public Mono<ResponseEntity<FavoriteProduct>> addProductToFavorites(
            Mono<JwtAuthenticationToken> jwtAuthenticationTokenMono,
            @Valid @RequestBody Mono<NewFavoriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        // zip объединяет два Моно, преобразуя их в tuple
        return Mono.zip(jwtAuthenticationTokenMono, payloadMono)
                .flatMap(tuple -> this.favoriteProductsService
                        .addProductToFavorites(
                                tuple.getT2().productId(),
                                tuple.getT1().getToken().getSubject())
                )
                .map(favoriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("api/v1/favorite-products/{id}")
                                .build(Map.of("id", favoriteProduct.getId())))
                        .body(favoriteProduct));
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromFavorites(
            Mono<JwtAuthenticationToken> jwtAuthenticationTokenMono,
            @PathVariable("productId") Integer productId
    ) {
        return jwtAuthenticationTokenMono
                .flatMap(token ->
                        this.favoriteProductsService
                                .removeProductFromFavorites(
                                        productId,
                                        token.getToken().getSubject()
                                )
                                .then(Mono
                                        .just(ResponseEntity.noContent().build()))
                );
    }
}
