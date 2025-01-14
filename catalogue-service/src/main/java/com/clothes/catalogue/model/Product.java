package com.clothes.catalogue.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "catalogue", name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String title;

    @NotNull
    @Size(min = 10, max = 1000)
    @Column(nullable = false)
    private String description;
}
