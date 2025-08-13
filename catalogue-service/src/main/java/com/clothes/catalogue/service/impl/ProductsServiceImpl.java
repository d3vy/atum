package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.repository.ProductRepository;
import com.clothes.catalogue.service.general.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    private final ProductRepository productRepository;


    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            log.info("Searching for product with title: {}", filter);
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            log.info("Searching for all products because there is no filter");
            return this.productRepository.findAll();
        }
    }

    @Override
    public Iterable<Product> findAllProducts(String filter, Integer categoryId) {
        String titleFilter = (filter != null && !filter.isBlank() ? "%" + filter + "%" : null);
        return this.productRepository.findFiltered(titleFilter, categoryId);
    }


    @Override
    public Product createProduct(String title, String description) {
        log.info("Product with title {} has been created", title);
        return this.productRepository.save(new Product(null, title, description, null));
    }
}
