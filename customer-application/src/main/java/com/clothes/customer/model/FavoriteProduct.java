package com.clothes.customer.model;

import java.util.UUID;

public record FavoriteProduct(
        UUID id,
        Integer productId
) {
}
