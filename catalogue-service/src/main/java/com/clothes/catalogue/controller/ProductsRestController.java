package com.clothes.catalogue.controller;

import com.clothes.catalogue.controller.payload.NewProductPayload;
import com.clothes.catalogue.model.Product;
import com.clothes.catalogue.service.general.ProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Контроллер для работы с коллекцией товаров.
 * Предоставляет эндпоинты для получения списка товаров и создания нового товара.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/catalogue/products")
public class ProductsRestController {

    // Сервис для работы с товарами, внедряемый автоматически через конструктор (аннотация @RequiredArgsConstructor)
    private final ProductsService productsService;

    /**
     * Эндпоинт для получения списка всех товаров с возможностью фильтрации.
     *
     * @param filter (опционально) параметр фильтра для поиска товаров.
     * @return Итерабельную коллекцию объектов Product, удовлетворяющих заданному фильтру.
     */
    @GetMapping // Метод для получения всех товаров с учетом фильтра
    public Iterable<Product> findAllProducts(
            @RequestParam(name = "filter", required = false) String filter) {
        // Вызов сервиса для поиска всех товаров, при этом возможно применение фильтрации по названию или описанию
        return this.productsService.findAllProducts(filter);
    }

    /**
     * Эндпоинт для создания нового товара.
     *
     * @param payload               Объект, содержащий данные нового товара (название и описание), валидируемый аннотацией @Valid.
     * @param bindingResult         Результаты валидации payload. Если есть ошибки, возвращается ответ с HTTP-статусом 400.
     * @param uriComponentsBuilder  Помогает в построении URI для вновь созданного ресурса.
     * @return ResponseEntity с созданным объектом Product и HTTP-статусом 201 Created,
     * либо с ошибкой и HTTP-статусом 400 Bad Request, если данные не прошли валидацию.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) {
        // Проверка на наличие ошибок валидации данных, переданных в теле запроса
        if (bindingResult.hasErrors()) {
            // Если валидация не пройдена, возвращаем ответ с HTTP-статусом 400 Bad Request
            return ResponseEntity.badRequest().body(null);
        } else {
            // Создание нового товара с помощью сервиса, передавая необходимые параметры (название и описание)
            Product product = this.productsService.createProduct(payload.title(), payload.description());
            // Формирование URI для созданного ресурса с использованием идентификатора продукта
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/catalogue/products/{productId}")
                            .build(Map.of("productId", product.getId()))
            ).body(product); // Возвращаем созданный объект и HTTP-статус 201 Created
        }
    }
}
