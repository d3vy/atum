package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.CategoryClient;
import com.clothes.manager.controller.payload.UpdateCategoryPayload;
import com.clothes.manager.dto.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class CategoryClientImpl implements CategoryClient {

    private final RestClient restClient;

    @Override
    public Category findCategoryById(Integer categoryId) {
        log.info("Find category by id {}", categoryId);
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/categories/{categoryId}", categoryId)
                .retrieve()
                .body(Category.class);
    }

    @Override
    public void updateCategory(Integer categoryId, String title) {
        log.info("Update category with id {} with title {}", categoryId, title);
        this.restClient
                .patch()
                .uri("/api/v1/catalogue/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateCategoryPayload(title, categoryId))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        log.info("Delete category by id {}", categoryId);
        this.restClient
                .delete()
                .uri("/api/v1/catalogue/categories/{categoryId}", categoryId)
                .retrieve()
                .toBodilessEntity();
    }
}
