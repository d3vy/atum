package com.clothes.catalogue.repository;

import com.clothes.catalogue.model.Category;
import com.clothes.catalogue.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(
            value = "SELECT * FROM catalogue.products WHERE title ILIKE :filter",
            nativeQuery = true
    )
    List<Category> findAllByTitleLikeIgnoreCase(@Param("filter") String filter);
}
