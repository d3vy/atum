package com.clothes.manager.controller.payload;

import java.util.UUID;

public record NewCategoryPayload(
        String title,
        UUID parentId
) {
}
