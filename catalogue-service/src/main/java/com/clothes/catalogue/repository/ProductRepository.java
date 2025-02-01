package com.clothes.catalogue.repository;

import com.clothes.catalogue.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью Product.
 * Расширяет CrudRepository, предоставляя стандартные CRUD-операции для сущности Product с идентификатором типа Integer.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

    /**
     * Выполняет поиск товаров по названию с использованием регистронезависимого сравнения.
     *
     * @param filter Строка фильтра для поиска в поле title. Обычно содержит подстановочные символы (например, '%keyword%') для поиска по шаблону.
     * @return Итерабельная коллекция найденных товаров, соответствующих условию фильтра.
     */
    @Query(
            value = "SELECT * FROM catalogue.products WHERE title ILIKE :filter",
            nativeQuery = true
    )
    Iterable<Product> findAllByTitleLikeIgnoreCase(@Param("filter") String filter);
}
