package com.clothes.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Document(collection = "product-reviews")
public class ProductReview {
    @Id
    private UUID id;

    private String userId;
    private Integer productId;
    private Integer rating;
    private String review;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public ProductReview() {
    }

    public ProductReview(UUID id, String userId, Integer productId, Integer rating, String review) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.review = review;
    }
}
