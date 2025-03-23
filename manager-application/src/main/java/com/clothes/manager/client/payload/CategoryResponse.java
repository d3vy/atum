package com.clothes.manager.client.payload;

import java.util.List;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String title,
        UUID parentId,
        List<CategoryResponse> subcategories
) {
}
