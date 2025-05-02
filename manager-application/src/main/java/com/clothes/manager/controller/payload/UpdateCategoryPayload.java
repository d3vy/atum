package com.clothes.manager.controller.payload;

import java.util.UUID;

public record UpdateCategoryPayload(
        String title,
        Integer parentId
) {
}
