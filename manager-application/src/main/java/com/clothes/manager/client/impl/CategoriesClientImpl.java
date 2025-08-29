package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.dto.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CategoriesClientImpl implements CategoriesClient {

    private final RestClient restClient;

    public CategoriesClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Category createCategory(NewCategoryPayload payload) {
        return this.restClient
                .post()
                .uri("/api/v1/catalogue/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Category.class);
    }

    @Override
    public List<Category> getAllCategories(String filter) {
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/categories?filter={filter}", filter)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Category toResponse(Category category) {
        return new Category(
                category.id(),
                category.title(),
                category.parent(),
                category.subcategories().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Category> getAllCategoriesAsTree() {
        List<Category> flatCategories = getAllCategories("");

        Map<Integer, Category> idToCategory = new HashMap<>();
        for (Category cat : flatCategories) {
            idToCategory.put(cat.id(), new Category(cat.id(), cat.title(), cat.subcategories().getLast(), new ArrayList<>()));
        }

        List<Category> rootCategories = new ArrayList<>();
        for (Category cat : flatCategories) {
            Category current = idToCategory.get(cat.id());

            if (cat.parent() == null) {
                rootCategories.add(current);
            } else {
                Category parent = idToCategory.get(cat.parent().id());
                if (parent != null) {
                    parent.subcategories().add(current);
                }
            }
        }
        return rootCategories;
    }
}
