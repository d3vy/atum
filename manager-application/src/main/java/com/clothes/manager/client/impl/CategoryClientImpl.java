package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.CategoryClient;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.controller.payload.UpdateCategoryPayload;
import com.clothes.manager.dto.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CategoryClientImpl implements CategoryClient {

    private final RestClient restClient;

    @Override
    public List<Category> getRootCategories() {
        log.info("Fetching categories");
        return this.restClient
                .get()
                .uri("/api/v1/categories")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Category createCategory(String title, String parentTitle) {
        log.info("Creating category with title {} and parentTitle {}", title, parentTitle);

        Category parentCategory = findCategoryByTitle(parentTitle);
        // Создаем объект payload для передачи данных новой категории
        NewCategoryPayload payload = new NewCategoryPayload(title, parentCategory.title());

        // Выполняем POST-запрос к сервису каталога для создания новой категории
        return this.restClient
                .post()
                .uri("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Category.class);
    }

    @Override
    public Category findCategoryById(Integer categoryId) {
        log.info("Fetching category by id {}", categoryId);
        return this.restClient
                .get()
                .uri("/api/v1/categories/{categoryId}", categoryId)
                .retrieve()
                .body(Category.class);
    }

    @Override
    public Category findCategoryByTitle(String title) {
        return this.restClient
                .get()
                .uri("/api/v1/categories/{categoryId}/by-title", findCategoryByTitle(title).id())
                .retrieve()
                .body(Category.class)
                ;
    }

    @Override
    public void updateCategory(Integer categoryId, String title) {
        log.info("Updating category with id {} and title {}", categoryId, title);
        this.restClient
                .patch()
                .uri("/api/v1/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateCategoryPayload(title))
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        log.info("Deleting category with id {}", categoryId);
        this.restClient
                .delete()
                .uri("/api/v1/categories/{categoryId}", categoryId)
                .retrieve()
                .toBodilessEntity();
    }
}
