package com.clothes.catalogue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@ToString(exclude = "subcategories")
@EqualsAndHashCode(of = "id")
@Table(schema = "catalogue", name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> subcategories;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Category getParent() {
        return parent;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public Category(Integer id, String title, Category parent, List<Category> subcategories) {
        this.id = id;
        this.title = title;
        this.parent = parent;
        this.subcategories = subcategories;
    }

    public Category() {
    }
}
