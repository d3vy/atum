package com.clothes.catalogue.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс Product представляет сущность товара в каталоге.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "catalogue", name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    /**
     * Уникальный идентификатор товара.
     * Генерируется автоматически при вставке новой записи в таблицу.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false) // Поле не может быть null в базе данных
    private Integer id;

    /**
     * Название товара.
     * Обязательно должно содержать от 3 до 50 символов.
     */
    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String title;

    /**
     * Описание товара.
     * Обязательно должно содержать от 10 до 1000 символов.
     */
    @NotNull
    @Size(min = 10, max = 1000)
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
