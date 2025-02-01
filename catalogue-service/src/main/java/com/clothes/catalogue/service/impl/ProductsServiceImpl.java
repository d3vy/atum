package com.clothes.catalogue.service.impl;

import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.repository.ProductRepository;
import com.clothes.catalogue.service.general.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с товарами каталога.
 * Реализует методы поиска и создания товаров.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

    // Репозиторий для доступа к данным товаров
    private final ProductRepository productRepository;

    /**
     * Поиск товаров с возможностью фильтрации по названию.
     * <p>
     * Если передан фильтр (не null и не пустая строка), производится поиск товаров,
     * название которых содержит указанное значение (регистронезависимый поиск).
     * Если фильтр отсутствует, возвращаются все товары.
     *
     * @param filter Строка фильтра для поиска товаров по названию.
     * @return Итерабельная коллекция найденных товаров.
     */
    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            // Логирование поиска по конкретному названию
            log.info("Searching for product with title: {}", filter);
            // Формируем строку с подстановочными символами для поиска по шаблону
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            // Логирование поиска всех товаров при отсутствии фильтра
            log.info("Searching for all products because there is no filter");
            return this.productRepository.findAll();
        }
    }

    /**
     * Создаёт новый товар с заданными названием и описанием.
     *
     * @param title       Название нового товара.
     * @param description Описание нового товара.
     * @return Созданный объект Product.
     */
    @Override
    public Product createProduct(String title, String description) {
        // Логирование создания нового товара
        log.info("Product with title {} has been created", title);
        // Сохраняем новый товар в базу данных. Идентификатор передается как null, чтобы база его сгенерировала.
        return this.productRepository.save(new Product(null, title, description));
    }
}
