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

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductsClient productClient;


    @GetMapping("list")
    public String getProductsList(
            Model model,
            @RequestParam(value = "filter", required = false) String filter) {
        model.addAttribute("products", productClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("create_new_product")
    public String getNewProductPage() {
        return "catalogue/products/create_new_product";
    }

    @PostMapping("create_new_product")
    public String createNewProduct(NewProductPayload payload,
                                   Model model) {
        Product product =
                this.productClient.createProduct(payload.title(), payload.description());
        return "redirect:/catalogue/products/%d".formatted(product.id());
    }
}
