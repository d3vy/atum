package com.clothes.catalogue.service.general;

import com.clothes.catalogue.model.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductById(Integer productId);

    void updateProduct(Integer id, String title, String description);

    void deleteProductById(Integer id);

    boolean existsById(Integer productId);
}
