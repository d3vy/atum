package com.clothes.manager.dto;

import com.clothes.manager.client.general.CategoryClient;

import java.util.List;

public record Category(
        Integer id,
        String title,
        Category parent,
        List<Category> subcategories
) {
}
