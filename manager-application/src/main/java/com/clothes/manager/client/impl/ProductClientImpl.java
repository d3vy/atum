package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.ProductClient;
import com.clothes.manager.controller.payload.UpdateProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    private final RestClient restClient;

    @Override
    public Product findProductById(Integer productId) {
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .retrieve()
                .body(Product.class);
    }

    @Override
    public void updateProduct(Integer productId, String title, String description) {
        this.restClient
                .patch()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateProductPayload(title, description))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void deleteProductById(Integer productId) {
        this.restClient
                .delete()
                .uri("/api/v1/catalogue/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity();
    }
}
