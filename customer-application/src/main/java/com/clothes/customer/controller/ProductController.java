package com.clothes.customer.controller;

import com.clothes.customer.client.exception.ClientBadRequestException;
import com.clothes.customer.client.general.FavoriteProductsClient;
import com.clothes.customer.client.general.ProductReviewsClient;
import com.clothes.customer.client.general.ProductsClient;
import com.clothes.customer.client.payload.NewProductReviewPayload;
import com.clothes.customer.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;
    private final FavoriteProductsClient favoriteProductsClient;
    private final ProductReviewsClient productReviewsClient;


    @ModelAttribute(value = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") Integer productId) {
        return this.productsClient
                .findProduct(productId)
                .switchIfEmpty(Mono.defer(
                        () -> Mono.error(new NoSuchElementException("customer.products.error.not_found"))));
    }


    @GetMapping
    public Mono<String> getProductPage(Model model,
                                       @ModelAttribute("product") Mono<Product> productMono) {
        return productMono.flatMap(product ->
                this.favoriteProductsClient.findFavoriteProductByProductId(product.id())
                        .map(fav -> true)
                        .defaultIfEmpty(false)
                        .doOnNext(inFavorites -> model.addAttribute("inFavorites", inFavorites))
                        .then(this.productReviewsClient.findProductReviewsByProductId(product.id())
                                .collectList()
                                .doOnNext(reviews -> model.addAttribute("reviews", reviews)))
                        .thenReturn("customer/products/product")
        );
    }


    @PostMapping("add-to-favorites")
    public Mono<String> addProductToFavorites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId ->
                        this.favoriteProductsClient.addProductToFavorites(productId)
                                .thenReturn("redirect:/customer/products/%d".formatted(productId))
                                .onErrorResume(exception -> {
                                    log.error("Ошибка при добавлении в избранное: {}", exception.getMessage(), exception);
                                    return Mono.just("redirect:/customer/products/%d".formatted(productId));
                                })
                );
    }


    @PostMapping("remove-from-favorites")
    public Mono<String> removeProductFromFavorites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId ->
                        this.favoriteProductsClient.removeProductFromFavorites(productId)
                                .thenReturn("redirect:/customer/products/%d".formatted(productId))
                );
    }


    @PostMapping("create-review")
    public Mono<String> createReview(@ModelAttribute("product") Mono<Product> productMono,
                                     NewProductReviewPayload payload,
                                     Model model,
                                     ServerHttpResponse response) {
        return productMono.flatMap(product ->
                this.productReviewsClient.createProductReview(product.id(), payload.rating(), payload.review())
                        .thenReturn("redirect:/customer/products/%d".formatted(product.id()))
                        .onErrorResume(ClientBadRequestException.class, exception -> {
                            model.addAttribute("inFavorites", false);
                            model.addAttribute("payload", payload);
                            model.addAttribute("errors", exception.getErrors());
                            response.setStatusCode(HttpStatus.BAD_REQUEST);
                            return this.favoriteProductsClient.findFavoriteProductByProductId(product.id())
                                    .doOnNext(fav -> model.addAttribute("inFavorites", true))
                                    .thenReturn("customer/products/product");
                        })
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handeNoSuchElementException(Model model,
                                              NoSuchElementException exception,
                                              ServerHttpResponse response) {
        model.addAttribute("error", exception.getMessage());
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return "errors/404";
    }


    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return Objects.requireNonNull(exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
                .doOnSuccess(token -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, token));
    }
}
