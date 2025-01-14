package com.clothes.catalogue.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(

        @NotNull(message = "{catalogue.products.create.errors.title_is_required}")
        @Size(min = 3, max = 50, message = "{catalogue.products.create.errors.title_size_is_invalid}")
        String title,

        @Size(min = 10, max = 1000, message = "{catalogue.products.create.errors.description_size_is_invalid}")
        String description
) {
}
