package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.repository.CategoryRepository;
import com.clothes.catalogue.repository.ProductRepository;
import com.clothes.catalogue.service.general.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(
            ProductServiceImpl.class);

    @Override
    public Optional<Product> findProductById(Integer productId) {
        log.info("Searching for product with id: {}", productId);
        return this.productRepository.findById(productId);
    }

    @Override
    public List<Product> findProductByCategoryTree(Integer categoryId) {
        Category root = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category with id " + categoryId + " not found"));

        Set<Integer> categoryIds = new HashSet<>();
        collectCategoryIds(root, categoryIds);

        return this.productRepository.findProductByCategoryIdIn(categoryIds);
    }


    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String description) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDescription(description);
                    log.info("Product with id: {} has been updated", id);
                }, () -> {
                    log.error("Product with id: {} not found or has invalid fields to update", id);
                    throw new NoSuchElementException("Product with id " + id + " not found");
                }
        );
    }


    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        log.info("Deleting product with id: {}", id);
        this.productRepository.deleteById(id);
    }


    @Override
    public boolean existsById(Integer productId) {
        log.info("Checking if product with id: {} exists", productId);
        return this.productRepository.existsById(productId);
    }

    @Override
    public void save(Product product) {
        this.productRepository.save(product);
    }

    @Override
    @Transactional
    public void assignProductToCategory(Integer productId, Integer categoryId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category with id " + categoryId + " not found"));

        product.setCategory(category);
        this.productRepository.save(product);

        log.info("Connected product with id {} to category with id {}", productId, categoryId);
    }

    private void collectCategoryIds(Category category, Set<Integer> ids) {
        ids.add(category.getId());
        for (Category subcategory : category.getSubcategories()) {
            collectCategoryIds(subcategory, ids);
        }
    }
}
