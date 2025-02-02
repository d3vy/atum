package com.clothes.manager.controller;

import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для управления операциями с продуктами в каталоге.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductsClient productClient;

    /**
     * Обрабатывает GET-запрос для отображения списка продуктов.
     *
     * @param model  Модель для передачи данных в представление.
     * @param filter Фильтр для поиска продуктов (необязательный параметр).
     * @return Имя представления для отображения списка продуктов.
     */
    @GetMapping("list")
    public String getProductsList(
            Model model,
            @RequestParam(value = "filter", required = false) String filter) {
        model.addAttribute("products", productClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    /**
     * Обрабатывает GET-запрос для отображения страницы создания нового продукта.
     *
     * @return Имя представления для создания нового продукта.
     */
    @GetMapping("create_new_product")
    public String getNewProductPage() {
        return "catalogue/products/create_new_product";
    }

    /**
     * Обрабатывает POST-запрос для создания нового продукта.
     *
     * @param payload Данные нового продукта.
     * @param model   Модель для передачи данных в представление.
     * @return Перенаправление на страницу созданного продукта.
     */
    @PostMapping("create_new_product")
    public String createNewProduct(NewProductPayload payload,
                                   Model model) {
        Product product =
                this.productClient.createProduct(payload.title(), payload.description());
        return "redirect:/catalogue/products/%d".formatted(product.id());
    }
}
