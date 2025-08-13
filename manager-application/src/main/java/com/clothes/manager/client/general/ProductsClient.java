package com.clothes.manager.client.general;

import com.clothes.manager.dto.Product;

import java.util.List;
import java.util.Set;

public interface ProductsClient {

    List<Product> findAllProducts(String filter);
    List<Product> findAllProducts(String filter, Integer filterCategory);

    Product createProduct(String title, String description);

    List<Product> findAllProductsByFilterAndCategoryIds(String filter, Set<Integer> categoryIdsForFilter);
}
