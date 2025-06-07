package com.clothes.catalogue.service.payload;

import java.util.List;

public record CategoryResponse(
        Integer id,
        String title,
        Integer parentId,
        List<CategoryResponse> subcategories
) {
}
