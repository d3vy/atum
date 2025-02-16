package com.clothes.feedback.controller;

import com.clothes.feedback.controller.payload.NewProductReviewPayload;
import com.clothes.feedback.model.ProductReview;
import com.clothes.feedback.service.general.ProductReviewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
@RequiredArgsConstructor
@RequestMapping("api/v1/product-reviews")
public class ProductReviewsController {

    private final ProductReviewsService productReviewsService;

    @GetMapping("by-product-id/{productId:\\d+}")
    @Operation(
            security = @SecurityRequirement(name = "keycloak")
    )
    public Flux<ProductReview> findProductReviewsByProductId(
            @PathVariable("productId") Integer productId) {
        return this.productReviewsService.findProductReviewsByProductId(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            Mono<JwtAuthenticationToken> jwtAuthenticationTokenMono,
            @Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        return jwtAuthenticationTokenMono
                .flatMap(token -> payloadMono
                        .flatMap(payload -> this.productReviewsService
                                .createProductReview(
                                        token.getToken().getSubject(),
                                        payload.productId(),
                                        payload.rating(),
                                        payload.review()
                                )))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/api/v1/product-reviews/{id}")
                                .build(Map.of("id", productReview.getId())))
                        .body(productReview));
    }
}
