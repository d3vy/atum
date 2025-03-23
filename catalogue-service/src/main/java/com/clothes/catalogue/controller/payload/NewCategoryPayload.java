package com.clothes.catalogue.controller.payload;

public record NewCategoryPayload(
        String title,
        String parentTitle
) {
}
