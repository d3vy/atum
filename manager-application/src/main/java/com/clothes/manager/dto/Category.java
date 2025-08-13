package com.clothes.manager.dto;

import java.util.List;

public record Category(
        Integer id,
        String title,
        Category parent,
        List<Category> subcategories
) {
}
