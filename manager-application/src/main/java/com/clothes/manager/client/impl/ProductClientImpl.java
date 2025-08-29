package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.ProductClient;
import com.clothes.manager.controller.payload.UpdateProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


public class ProductClientImpl implements ProductClient {

    private final RestClient restClient;

    public ProductClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    private static final Logger log = LoggerFactory.getLogger(
            ProductClientImpl.class);


    @Override
    public Product findProductById(Integer productId) {
        log.info("Finding product with id: {}", productId);
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .retrieve()
                .body(Product.class);
    }


    @Override
    public void updateProduct(Integer productId, String title, String description, Integer categoryId) {
        log.info("Updating product with id: {}", productId);
        this.restClient
                .patch()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateProductPayload(title, description, categoryId))
                .retrieve()
                .toBodilessEntity();
    }


    @Override
    public void deleteProductById(Integer productId) {
        log.info("Deleting product with id: {}", productId);
        this.restClient
                .delete()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void assignProductToCategory(Integer productId, Integer categoryId) {
        log.info("Assigning product with id: {} to category with id: {}", productId, categoryId);
        this.restClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/catalogue/products/{productId}")
                        .queryParam("categoryId", categoryId)
                        .build(productId)
                )
                .retrieve()
                .toBodilessEntity();
    }
}
