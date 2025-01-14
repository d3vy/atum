package com.clothes.manager.controller;

import com.clothes.manager.client.general.ProductClient;
import com.clothes.manager.controller.payload.UpdateProductPayload;
import com.clothes.manager.dto.Product;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductClient productClient;

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

    @PostMapping("edit_product")
    public String updateProduct(
            @ModelAttribute(value = "product", binding = false) Product product,
            UpdateProductPayload payload,
            Model model) {
        try {
            this.productClient
                    .updateProduct(product.id(), payload.title(), payload.description());
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
}
