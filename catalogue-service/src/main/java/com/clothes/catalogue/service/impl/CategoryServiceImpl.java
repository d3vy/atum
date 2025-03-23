package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.repository.CategoryRepository;
import com.clothes.catalogue.service.general.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getRootCategories() {
        log.info("Searching for root categories");
        return categoryRepository.findCategoryByParentIsNull();
    }

    @Override
    public Category createCategory(String title, String parentTitle) {

        Category category = new Category();
        category.setTitle(title);

        if (parentTitle != null) {
            log.info("Setting parent for category with parentTitle: {}", parentTitle);
            Category parent = categoryRepository.findCategoryByTitle(parentTitle)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findCategoryById(Integer categoryId) {
        log.info("Searching for category with id: {}", categoryId);
        return this.categoryRepository.findById(categoryId);
    }

    @Override
    public Optional<Category> findCategoryByTitle(String title) {
        return this.categoryRepository.findCategoryByTitle(title);
    }

    @Override
    @Transactional
    public void updateCategory(Integer categoryId, String title) {
        this.categoryRepository
                .findById(categoryId)
                .ifPresentOrElse(category -> {
                            category.setTitle(title);
                            log.info("Updating category with id: {}", categoryId);
                        }, () -> {
                            log.error("Category with id: {} not found", categoryId);
                            throw new NoSuchElementException("Category with id: " + categoryId + " not found");
                        }
                );

    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        log.info("Deleting category with id: {}", categoryId);
        this.categoryRepository.deleteById(categoryId);
    }

    @Override
    public boolean existsById(Integer categoryId) {
        log.info("Checking if category with id: {} exists", categoryId);
        return this.categoryRepository.existsById(categoryId);
    }
}
