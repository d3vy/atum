package com.clothes.catalogue.controller.payload;

import java.util.UUID;

public record NewCategoryPayload(
        String title,
        Integer parentId
) {
}
