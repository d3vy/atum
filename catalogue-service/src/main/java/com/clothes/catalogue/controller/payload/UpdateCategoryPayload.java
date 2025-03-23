package com.clothes.catalogue.controller.payload;

import java.util.UUID;

public record UpdateCategoryPayload(
        String title,
        UUID parentId
) {
}
