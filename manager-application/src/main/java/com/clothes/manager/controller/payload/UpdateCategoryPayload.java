package com.clothes.manager.controller.payload;

public record UpdateCategoryPayload(
        String title,
        Integer parentId
) {
}
