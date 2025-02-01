package com.clothes.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product-reviews")
public class ProductReview {
    @Id
    private UUID id;

    private String userId;
    private Integer productId;
    private Integer rating;
    private String review;
}
