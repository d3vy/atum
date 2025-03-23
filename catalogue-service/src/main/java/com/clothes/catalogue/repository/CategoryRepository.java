package com.clothes.catalogue.repository;

import com.clothes.catalogue.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    List<Category> findCategoryByParentIsNull();

    @Query(value = "SELECT * FROM catalogue.categories WHERE title = :title", nativeQuery = true)
    Optional<Category> findCategoryByTitle(@Param("title") String title);
}
