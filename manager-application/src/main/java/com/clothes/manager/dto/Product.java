package com.clothes.manager.dto;

public record Product(
        Integer id,
        String title,
        String description,
        Integer categoryId) {
}
