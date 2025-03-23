package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.UpdateCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.service.general.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories/{categoryId:\\d+}")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Category> findCategoryById(@PathVariable Integer categoryId) {
        // Пытаемся найти категорию по id
        Category category = this.categoryService.findCategoryById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        // Возвращаем найденный продукт с HTTP-статусом 200 OK.
        return ResponseEntity.ok(category);
    }

    @GetMapping("by-title")
    public ResponseEntity<Category> findCategoryByTitle(@RequestBody String title) {
        Category category = this.categoryService.findCategoryByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));

        return ResponseEntity.ok(category);
    }

    @PatchMapping
    public ResponseEntity<Void> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody UpdateCategoryPayload payload
    ) {
        // Проверяем, существует ли категория с заданным ID.
        if (!this.categoryService.existsById(categoryId)) {
            // Если категория не найдена, возвращаем ответ с HTTP-статусом 404 Not Found.
            return ResponseEntity.notFound().build();
        }

        // Обновляем категорию, передавая в сервис новые значения.
        categoryService.updateCategory(categoryId, payload.title());
        // Возвращаем ответ с HTTP-статусом 204 No Content,
        // что означает успешное обновление без возвращаемого содержимого.
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        // Проверяем, существует ли категория с заданным ID.
        if (!this.categoryService.existsById(categoryId)) {
            // Если категория не найдена, возвращаем ответ с HTTP-статусом 404 Not Found.
            return ResponseEntity.notFound().build();
        }
        // Удаляем категорию, используя метод сервиса.
        this.categoryService.deleteCategory(categoryId);
        // Возвращаем ответ с HTTP-статусом 204 No Content, что означает успешное удаление.
        return ResponseEntity.noContent().build();
    }


}
