package com.clothes.catalogue.service.general;

import com.clothes.catalogue.controller.payload.UpdateCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.service.payload.CategoryResponse;

import java.util.UUID;

public interface CategoryService {

    CategoryResponse getCategoryById(Integer id);

    Category findCategoryById(Integer id);

    CategoryResponse updateCategory(Integer id, UpdateCategoryPayload payload);

    void deleteCategory(Integer id);
}
