package com.clothes.feedback.service.impl;

import com.clothes.feedback.model.ProductReview;
import com.clothes.feedback.repository.ProductReviewsRepository;
import com.clothes.feedback.service.general.ProductReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductReviewsServiceImpl implements ProductReviewsService {

    private final ProductReviewsRepository productReviewsRepository;

    public ProductReviewsServiceImpl(ProductReviewsRepository productReviewsRepository) {
        this.productReviewsRepository = productReviewsRepository;
    }

    @Override
    public Mono<ProductReview> createProductReview(String userId, Integer productId,
                                                   Integer rating, String review) {
        return this.productReviewsRepository
                .save(
                        new ProductReview(UUID.randomUUID(), userId, productId, rating, review));
    }

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Integer productId) {
        return this.productReviewsRepository.findAllByProductId(productId);
    }
}
