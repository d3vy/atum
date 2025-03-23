package com.clothes.manager.client.general;

import com.clothes.manager.client.payload.CategoryResponse;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.dto.Category;

import java.util.List;

public interface CategoriesClient {

    CategoryResponse createCategory(NewCategoryPayload payload);

    List<CategoryResponse> getAllCategories(String filter);

    CategoryResponse toResponse(Category category);
}
