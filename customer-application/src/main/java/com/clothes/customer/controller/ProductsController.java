package com.clothes.customer.controller;

import com.clothes.customer.client.general.FavoriteProductsClient;
import com.clothes.customer.client.general.ProductsClient;
import com.clothes.customer.model.FavoriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {

    private final ProductsClient productClient;
    private final FavoriteProductsClient favoriteProductsClient;

    @GetMapping("list")
    public Mono<String> getProductsListPage(
            Model model,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        model.addAttribute("filter", filter);
        return this.productClient
                .findAllProducts(filter)
                .collectList()
                .doOnNext(products ->
                        model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }

    @GetMapping("favorites")
    public Mono<String> getFavoriteProductsListPage(
            Model model,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        model.addAttribute("filter", filter);
        return this.favoriteProductsClient
                .findAllFavoriteProducts()
                .map(FavoriteProduct::productId)
                .collectList()
                .flatMap(favoriteProducts ->
                        this.productClient.findAllProducts(filter)
                                .filter(product -> favoriteProducts.contains(product.id()))
                                .collectList()
                                .doOnNext(products ->
                                        model.addAttribute("products", products))
                                .thenReturn("customer/products/favorites"));
    }

}
