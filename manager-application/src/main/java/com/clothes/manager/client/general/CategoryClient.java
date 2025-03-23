package com.clothes.manager.client.general;

import com.clothes.manager.dto.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryClient {

    List<Category> getRootCategories();

    Category createCategory(String title, String parentTitle);

    Category findCategoryById(Integer categoryId);

    Category findCategoryByTitle(String title);

    void updateCategory(Integer categoryId, String title);

    void deleteCategory(Integer categoryId);

}
