package com.clothes.feedback.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "favorite-products")
public class FavoriteProduct {

    @Id
    private UUID id;
    private Integer productId;
    private String userId;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public FavoriteProduct() {
    }

    public FavoriteProduct(UUID id, Integer productId, String userId) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
    }
}
