package com.clothes.manager.client.general;

import com.clothes.manager.dto.Product;

public interface ProductClient {

    Product findProductById(Integer productId);

    void updateProduct(Integer productId, String title, String description);

    void deleteProductById(Integer productId);
}
