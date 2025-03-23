package com.clothes.catalogue.service.general;

import com.clothes.catalogue.controller.payload.NewCategoryPayload;
import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.service.payload.CategoryResponse;

import java.util.List;

public interface CategoriesService {

    CategoryResponse createCategory(NewCategoryPayload payload);

    List<CategoryResponse> getAllCategories(String filter);

    CategoryResponse toResponse(Category category);

}
