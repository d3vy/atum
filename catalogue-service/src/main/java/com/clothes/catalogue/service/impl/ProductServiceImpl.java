package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.repository.ProductRepository;
import com.clothes.catalogue.service.general.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findProductById(Integer productId) {
        log.info("Product with id: {} have been found", productId);
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDescription(description);
                    log.info("Product with id: {} have been updated", id);
                }, () -> {
                    log.error("Product with id: {} not found or has invalid fields to update", id);
                    throw new NoSuchElementException();
                }
        );
    }

    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        log.info("Product with id: {} have been deleted", id);
        this.productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer productId) {
        log.info("Checking is product with id: {} exist or not", productId);
        return this.productRepository.existsById(productId);
    }
}
