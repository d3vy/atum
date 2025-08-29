package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.client.general.ProductClient;
import com.clothes.manager.controller.payload.UpdateProductPayload;
import com.clothes.manager.dto.Product;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductClient productClient;
    private final CategoriesClient categoriesClient;

    public ProductController(ProductClient productClient, CategoriesClient categoriesClient) {
        this.productClient = productClient;
        this.categoriesClient = categoriesClient;
    }

    private static final Logger log = LoggerFactory.getLogger(
            ProductController.class);



    @ModelAttribute("product")
    public Product product(@PathVariable("productId") Integer productId) {
        return this.productClient.findProductById(productId);
    }


    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }


    @GetMapping("edit_product")
    public String getProductEditPage() {
        return "catalogue/products/edit_product";
    }

    @GetMapping("assign_product_to_category")
    public String assignProductToCategory(Model model) {
        model.addAttribute("categories", this.categoriesClient.getAllCategories(""));
        return "catalogue/products/assign_product_to_category";
    }


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
