package com.clothes.manager.dto;

import java.util.List;
import java.util.UUID;

public record Category(
        UUID id,
        String title,
        Category parent,
        List<Category> subcategories
) {
}
