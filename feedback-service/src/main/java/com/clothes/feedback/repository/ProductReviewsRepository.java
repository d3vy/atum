package com.clothes.feedback.repository;

import com.clothes.feedback.model.ProductReview;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductReviewsRepository extends ReactiveCrudRepository<ProductReview, UUID> {

    Flux<ProductReview> findAllByProductId(Integer productId);
}
