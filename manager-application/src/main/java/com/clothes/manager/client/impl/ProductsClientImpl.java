package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
public class ProductsClientImpl implements ProductsClient {

    private final RestClient restClient;


    @Override
    public List<Product> findAllProducts(String filter) {
        log.info("Fetching all products with filter: {}", filter);
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/products?filter={filter}", filter)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<Product> findAllProducts(String filter, Integer filterCategory) {
        log.info("Fetching products with filter: '{}' and categoryId: {}", filter, filterCategory);

        var uriBuilder = new StringBuilder("/api/v1/catalogue/products?");

        if (filter != null && !filter.isBlank()) {
            uriBuilder.append("filter=").append(filter).append("&");
        }

        if (filterCategory != null) {
            uriBuilder.append("categoryId=").append(filterCategory).append("&");
        }

        if (uriBuilder.charAt(uriBuilder.length() - 1) == '&' || uriBuilder.charAt(uriBuilder.length() - 1) == '?') {
            uriBuilder.deleteCharAt(uriBuilder.length() - 1);
        }

        return this.restClient
                .get()
                .uri(uriBuilder.toString())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }


    @Override
    public Product createProduct(String title, String description) {
        log.info("Creating a new product with title: {}", title);
        NewProductPayload payload = new NewProductPayload(title, description);
        return this.restClient
                .post()
                .uri("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Product.class);
    }

    @Override
    public List<Product> findAllProductsByFilterAndCategoryIds(String filter, Set<Integer> categoryIdsForFilter) {

        var uriBuilder = new StringBuilder("/api/v1/catalogue/products?");

        if (filter != null && !filter.isBlank()) {
            uriBuilder.append("filter=").append(filter).append("&");
        }

        for (Integer categoryId : categoryIdsForFilter) {
            uriBuilder.append("categoryId=").append(categoryId).append("&");
        }

        if (uriBuilder.charAt(uriBuilder.length() - 1) == '&') {
            uriBuilder.deleteCharAt(uriBuilder.length() - 1);
        }

        return this.restClient
                .get()
                .uri(uriBuilder.toString())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
