package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.UpdateProductPayload;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.service.general.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Контроллер для работы с продуктами каталога.
 * Обрабатывает HTTP-запросы для получения, обновления и удаления продуктов.
 */
@RestController
@RequiredArgsConstructor
// Базовый URL для всех методов контроллера с параметром productId, который должен состоять из цифр
@RequestMapping("api/v1/catalogue/products/{productId:\\d+}")
public class ProductRestController {

    // Сервис для работы с продуктами
    private final ProductService productService;

    /**
     * Обрабатывает GET-запрос для получения информации о продукте по его идентификатору.
     *
     * @param productId Идентификатор продукта, извлекаемый из URL.
     * @return HTTP-ответ с информацией о продукте и статусом 200 OK.
     * @throws NoSuchElementException Если продукт не найден.
     */
    @GetMapping
    public ResponseEntity<Product> findProduct(@PathVariable Integer productId) {
        // Пытаемся найти продукт по ID. Если продукт не найден, выбрасываем исключение с сообщением "Product not found".
        Product product = productService.findProductById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        // Возвращаем найденный продукт с HTTP-статусом 200 OK.
        return ResponseEntity.ok(product);
    }

    /**
     * Обрабатывает PATCH-запрос для обновления информации о продукте.
     *
     * @param productId Идентификатор продукта, извлекаемый из URL.
     * @param payload   Объект с данными для обновления (заголовок и описание).
     * @return HTTP-ответ со статусом 204 No Content, если обновление прошло успешно, или 404 Not Found, если продукт не найден.
     */
    @PatchMapping
    public ResponseEntity<Void> updateProduct(@PathVariable Integer productId,
                                              @Valid @RequestBody UpdateProductPayload payload) {
        // Проверяем, существует ли продукт с заданным ID.
        if (!productService.existsById(productId)) {
            // Если продукт не найден, возвращаем ответ с HTTP-статусом 404 Not Found.
            return ResponseEntity.notFound().build();
        }

        // Обновляем продукт, передавая в сервис новые значения заголовка и описания.
        productService.updateProduct(productId, payload.title(), payload.description());
        // Возвращаем ответ с HTTP-статусом 204 No Content, что означает успешное обновление без возвращаемого содержимого.
        return ResponseEntity.noContent().build();
    }

    /**
     * Обрабатывает DELETE-запрос для удаления продукта по его идентификатору.
     *
     * @param productId Идентификатор продукта, извлекаемый из URL.
     * @return HTTP-ответ со статусом 204 No Content, если удаление прошло успешно, или 404 Not Found, если продукт не найден.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        // Проверяем, существует ли продукт с заданным ID.
        if (!productService.existsById(productId)) {
            // Если продукт не найден, возвращаем ответ с HTTP-статусом 404 Not Found.
            return ResponseEntity.notFound().build();
        }

        // Удаляем продукт, используя метод сервиса.
        productService.deleteProductById(productId);
        // Возвращаем ответ с HTTP-статусом 204 No Content, что означает успешное удаление.
        return ResponseEntity.noContent().build();
    }
}
