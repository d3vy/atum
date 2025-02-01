package com.clothes.feedback.service.general;

import com.clothes.feedback.model.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsService {

    Mono<ProductReview> createProductReview(String userId, Integer productId,
                                            Integer rating, String review);

    Flux<ProductReview> findProductReviewsByProductId(Integer productId);
}
