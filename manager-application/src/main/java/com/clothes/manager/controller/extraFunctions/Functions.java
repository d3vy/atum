package com.clothes.manager.controller.extraFunctions;

import com.clothes.manager.dto.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class Functions {

    public void collectCategoryAndChildrenIds(
            List<Category> categories,
            Integer targetCategoryId,
            Set<Integer> result) {
        for (Category cat : categories) {
            if (cat.id().equals(targetCategoryId)) {
                collectIdsRecursively(cat, result);
                break;
            } else if (cat.subcategories() != null) {
                collectCategoryAndChildrenIds(cat.subcategories(), targetCategoryId, result);
            }
        }
    }

    public void collectIdsRecursively(Category category, Set<Integer> result) {
        result.add(category.id());
        if (category.subcategories() != null) {
            for (Category child : category.subcategories()) {
                collectIdsRecursively(child, result);
            }
        }
    }
}