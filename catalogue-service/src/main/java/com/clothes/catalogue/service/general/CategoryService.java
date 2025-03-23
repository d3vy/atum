package com.clothes.catalogue.service.general;

import com.clothes.catalogue.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getRootCategories();

    Category createCategory(String title, String categoryParent);

    Optional<Category> findCategoryById(Integer categoryId);

    Optional<Category> findCategoryByTitle(String title);

    void updateCategory(Integer categoryId, String title);

    void deleteCategory(Integer categoryId);

    boolean existsById(Integer categoryId);
}
