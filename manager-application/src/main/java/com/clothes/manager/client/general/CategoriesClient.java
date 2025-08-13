package com.clothes.manager.client.general;

import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.dto.Category;

import java.util.List;

public interface CategoriesClient {

    Category createCategory(NewCategoryPayload payload);

    List<Category> getAllCategories(String filter);

    Category toResponse(Category category);

    List<Category> getAllCategoriesAsTree();
}
