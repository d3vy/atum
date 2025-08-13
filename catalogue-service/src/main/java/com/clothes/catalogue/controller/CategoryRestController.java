package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.UpdateCategoryPayload;
import com.clothes.catalogue.service.general.CategoryService;
import com.clothes.catalogue.service.payload.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/categories/{categoryId}")
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(this.categoryService.getCategoryById(categoryId));
    }

    @PatchMapping
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody UpdateCategoryPayload payload) {
        return ResponseEntity.ok(this.categoryService.updateCategory(categoryId, payload));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
