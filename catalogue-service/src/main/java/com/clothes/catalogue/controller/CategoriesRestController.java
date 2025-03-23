package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.NewCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.service.general.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoriesRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody NewCategoryPayload payload,
                                            BindingResult bindingResult,
                                            UriComponentsBuilder builder) {
        // Проверка на наличие ошибок валидации данных, переданных в теле запроса
        if (bindingResult.hasErrors()) {
            // Если валидация не пройдена, возвращаем ответ с HTTP-статусом 400 Bad Request
            return ResponseEntity.badRequest().body(null);
        }
        // Создание новой категории на основе переданных данных
        Category category = categoryService.createCategory(payload.title(), payload.parentTitle());
        // Формирование URI для созданного ресурса с использованием идентификатора категории
        return ResponseEntity.created(
                builder
                        .replacePath("/api/v1/categories/{id}")
                        .build(Map.of("id", category.getId()))
        ).body(category); // Возвращаем созданный объект и HTTP-статус 201 Created
    }
}
