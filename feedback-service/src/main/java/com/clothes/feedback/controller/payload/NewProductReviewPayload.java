package com.clothes.feedback.controller.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload(

        @NotNull(message = "{feedback.products.reviews.create.errors.product_id_is_null}")
        Integer productId,

        @NotNull(message = "{feedback.products.reviews.create.errors.rating_is_null}")
        @Min(value = 1, message = "{feedback.products.reviews.create.errors.rating_is_too_low}")
        @Max(value = 5, message = "{feedback.products.reviews.create.errors.rating_is_too_high}")
        Integer rating,

        @Size(max = 1000, message = "{feedback.products.reviews.create.errors.review_is_too_long}")
        String review
) {
}
