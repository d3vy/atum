package com.clothes.manager.client.impl;

import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Реализация клиента для взаимодействия с сервисом каталога продуктов.
 */
@Slf4j
@RequiredArgsConstructor
public class ProductsClientImpl implements ProductsClient {

    private final RestClient restClient;

    /**
     * Получает список продуктов с возможностью фильтрации по названию.
     *
     * @param filter строка фильтрации по названию продукта; может быть null или пустой для получения всех продуктов.
     * @return список продуктов, соответствующих фильтру.
     */
    @Override
    public List<Product> findAllProducts(String filter) {
        log.info("Fetching all products with filter: {}", filter);
        // Выполняем GET-запрос к сервису каталога для получения списка продуктов
        return this.restClient
                .get()
                .uri("/api/v1/catalogue/products?filter={filter}", filter)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {});
    }

    /**
     * Создает новый продукт в сервисе каталога.
     *
     * @param title       название продукта.
     * @param description описание продукта.
     * @return созданный продукт с присвоенным идентификатором.
     */
    @Override
    public Product createProduct(String title, String description) {
        log.info("Creating a new product with title: {}", title);
        // Создаем объект payload для передачи данных нового продукта
        NewProductPayload payload = new NewProductPayload(title, description);
        // Выполняем POST-запрос к сервису каталога для создания нового продукта
        return this.restClient
                .post()
                .uri("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(Product.class);
    }
}
