package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.NewCategoryPayload;
import com.clothes.catalogue.service.general.CategoriesService;
import com.clothes.catalogue.service.payload.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/categories")
public class CategoriesRestController {

    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody NewCategoryPayload payload) {
        return ResponseEntity.ok(this.categoriesService.createCategory(payload));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(this.categoriesService.getAllCategories());
    }

}
