package com.clothes.catalogue.service.general;

import com.clothes.catalogue.model.Product;

public interface ProductsService {
    Iterable<Product> findAllProducts(String filter);

    Iterable<Product> findAllProducts(String filter, Integer categoryId);

    Product createProduct(String title, String description);
}
