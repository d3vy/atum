package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class ProductsClientImpl implements ProductsClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    // Обращение к catalogue-service для получения всех товаров
    @Override
    public List<Product> findAllProducts(String filter) {
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/products?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE); // Чтобы jackson понял, что это List<Product>
    }

    // Обращение к catalogue-service для создания нового товара
    @Override
    public Product createProduct(String title, String description) {
        return this.restClient
                .post()
                .uri("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new NewProductPayload(title, description))
                .retrieve()
                .body(Product.class);
    }
}
