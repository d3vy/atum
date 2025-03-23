package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.controller.payload.NewCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.repository.CategoryRepository;
import com.clothes.catalogue.service.general.CategoriesService;
import com.clothes.catalogue.service.payload.CategoryResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse createCategory(NewCategoryPayload payload) {
        Category category = new Category();
        category.setTitle(payload.title());

        if (payload.parentId() != null) {
            Category parent = this.categoryRepository.findById(payload.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            category.setParent(parent);
        }

        Category savedCategory = this.categoryRepository.save(category);
        return toResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return this.categoryRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getTitle(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getSubcategories() != null ? category.getSubcategories().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()) : List.of()
        );
    }
}
