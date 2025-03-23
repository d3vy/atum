package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.controller.payload.UpdateCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.repository.CategoryRepository;
import com.clothes.catalogue.service.general.CategoriesService;
import com.clothes.catalogue.service.general.CategoryService;
import com.clothes.catalogue.service.payload.CategoryResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoriesService categoriesService;

    @Override
    public CategoryResponse getCategoryById(UUID id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return categoriesService.toResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(UUID id, UpdateCategoryPayload payload) {

        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (payload.title() != null && !payload.title().isBlank()) {
            category.setTitle(payload.title());
        }

        if (payload.parentId() != null) {
            if (!payload.parentId().equals(id)) { // Нельзя назначить категорию самой себе
                Category parent = this.categoryRepository.findById(payload.parentId())
                        .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
                category.setParent(parent);
            } else {
                throw new IllegalArgumentException("Cannot assign category to itself");
            }
        } else {
            category.setParent(null);  // Если `parentId` null, делаем её корневой
        }

        return categoriesService.toResponse(this.categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!category.getSubcategories().isEmpty()) {
            category.getSubcategories()
                    .forEach(subcategory -> subcategory
                            .setParent(category.getParent())
                    );
        }

        this.categoryRepository.delete(category);
    }
}
