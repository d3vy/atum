package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.client.payload.CategoryResponse;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.dto.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class CategoriesClientImpl implements CategoriesClient {

    private final RestClient restClient;

    @Override
    public CategoryResponse createCategory(NewCategoryPayload payload) {
        return this.restClient
                .post()
                .uri("/api/v1/catalogue/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories(String filter) {
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/categories?filter={filter}", filter)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {

                });
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.id(),
                category.title(),
                category.parent() != null ? category.parent().id() : null,
                category.subcategories().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
        );
    }
}
