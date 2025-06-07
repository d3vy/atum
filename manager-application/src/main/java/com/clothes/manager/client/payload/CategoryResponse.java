package com.clothes.manager.client.payload;

import java.util.List;
import java.util.UUID;

public record CategoryResponse(
        Integer id,
        String title,
        Integer parentId,
        List<CategoryResponse> subcategories
) {
}
