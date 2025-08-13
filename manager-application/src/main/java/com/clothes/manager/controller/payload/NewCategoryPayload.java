package com.clothes.manager.controller.payload;

public record NewCategoryPayload(
        String title,
        Integer parentId
) {
}
