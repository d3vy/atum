package com.clothes.catalogue.service.general;

import com.clothes.catalogue.controller.payload.UpdateCategoryPayload;
import com.clothes.catalogue.service.payload.CategoryResponse;

import java.util.UUID;

public interface CategoryService {

    CategoryResponse getCategoryById(UUID id);

    CategoryResponse updateCategory(UUID id, UpdateCategoryPayload payload);

    void deleteCategory(UUID id);
}
