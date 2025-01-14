package com.clothes.manager.client.general;

import com.clothes.manager.dto.Product;

import java.util.List;

public interface ProductsClient {
    List<Product> findAllProducts(String filter);

    Product createProduct(String title, String description);
}
