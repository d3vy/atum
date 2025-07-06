package com.clothes.catalogue.service.general;

import com.clothes.catalogue.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<Product> findProductById(Integer productId);

    List<Product> findProductByCategoryTree(Integer categoryId);

    void updateProduct(Integer id, String title, String description);

    void deleteProductById(Integer id);

    boolean existsById(Integer productId);

    void save(Product product);

    void assignProductToCategory(Integer productId, Integer categoryId);
}
