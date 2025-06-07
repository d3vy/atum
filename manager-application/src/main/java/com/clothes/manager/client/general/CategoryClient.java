package com.clothes.manager.client.general;

import com.clothes.manager.client.payload.CategoryResponse;
import com.clothes.manager.dto.Category;

import java.util.List;

public interface CategoryClient {

    CategoryResponse findCategoryById(Integer categoryId);

    void updateCategory(Integer categoryId, String title);

    void deleteCategory(Integer categoryId);

}
