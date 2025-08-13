package com.clothes.manager.client.general;


import com.clothes.manager.dto.Category;

public interface CategoryClient {

    Category findCategoryById(Integer categoryId);

    void updateCategory(Integer categoryId, String title);

    void deleteCategory(Integer categoryId);

}
