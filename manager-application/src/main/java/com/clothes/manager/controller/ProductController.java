package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.client.general.CategoryClient;
import com.clothes.manager.client.general.ProductClient;
import com.clothes.manager.client.payload.CategoryResponse;
import com.clothes.manager.controller.payload.UpdateProductPayload;
import com.clothes.manager.dto.Product;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления операциями с продуктами каталога.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
@Slf4j
public class ProductController {

    private final ProductClient productClient;
    private final CategoriesClient categoriesClient;

    /**
     * Добавляет объект продукта в модель на основе идентификатора из пути.
     *
     * @param productId идентификатор продукта, извлеченный из пути
     * @return объект Product, полученный от ProductClient
     */
    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Integer productId) {
        return this.productClient.findProductById(productId);
    }

    /**
     * Обрабатывает GET-запрос для отображения информации о продукте.
     *
     * @return имя представления для отображения продукта
     */
    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    /**
     * Обрабатывает GET-запрос для отображения страницы редактирования продукта.
     *
     * @return имя представления для редактирования продукта
     */
    @GetMapping("edit_product")
    public String getProductEditPage() {
        return "catalogue/products/edit_product";
    }

    @GetMapping("assign_product_to_category")
    public String assignProductToCategory(Model model) {
        model.addAttribute("categories", this.categoriesClient.getAllCategories(""));
        return "catalogue/products/assign_product_to_category";
    }

    /**
     * Обрабатывает POST-запрос для обновления информации о продукте.
     *
     * @param product объект Product из модели
     * @param payload данные для обновления продукта
     * @param model   модель для передачи данных в представление
     * @return перенаправление на страницу продукта или обратно на страницу редактирования в случае ошибки
     */
    @PostMapping("edit_product")
    public String updateProduct(
            @ModelAttribute(value = "product", binding = false) Product product,
            UpdateProductPayload payload,
            Model model) {
        try {
            this.productClient.updateProduct(product.id(), payload.title(), payload.description(), payload.categoryId());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/edit_product";
        }
    }

    /**
     * Обрабатывает POST-запрос для удаления продукта.
     *
     * @param product объект Product из модели
     * @return перенаправление на список продуктов после удаления
     */
    @PostMapping("delete_product")
    public String deleteProduct(
            @ModelAttribute(value = "product", binding = false) Product product) {
        this.productClient.deleteProductById(product.id());
        return "redirect:/catalogue/products/list";
    }

    @PostMapping("assign_product_to_category.html")
    public String assignProductToCategory(
            @ModelAttribute(value = "product", binding = false) Product product,
            @RequestParam("categoryId") Integer categoryId,
            Model model
    ) {
        try {
            this.productClient.assignProductToCategory(product.id(), categoryId);
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("error", "Не удалось назначить категорию: " + e.getMessage());
            return "catalogue/products/edit_product";
        }

    }
}
