package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.client.general.ProductsClient;
import com.clothes.manager.controller.extraFunctions.Functions;
import com.clothes.manager.controller.payload.NewProductPayload;
import com.clothes.manager.dto.Category;
import com.clothes.manager.dto.Product;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductsClient productClient;
    private final CategoriesClient categoriesClient;
    private final Functions functions;

    public ProductsController(ProductsClient productClient, CategoriesClient categoriesClient, Functions functions) {
        this.productClient = productClient;
        this.categoriesClient = categoriesClient;
        this.functions = functions;
    }


    @GetMapping("list")
    public String getProductsList(
            Model model,
            @RequestParam(value = "filter", required = false) String filter) {
        model.addAttribute("products", this.productClient.findAllProducts(filter));
        model.addAttribute("filter", filter);

        return "catalogue/products/list";
    }

//    @GetMapping("list")
//    public String getProductsList(
//            Model model,
//            @RequestParam(value = "filter", required = false) String filter,
//            @RequestParam(value = "filterCategory", required = false) Integer filterCategory
//    ) {
//        List<Category> categories = this.categoriesClient.getAllCategoriesAsTree();
//
//        Set<Integer> categoryIdsForFilter = new HashSet<>();
//
//        if (filterCategory != null) {
//            this.functions.collectCategoryAndChildrenIds(categories, filterCategory, categoryIdsForFilter);
//        }
//
//        List<Product> products;
//        if (!categoryIdsForFilter.isEmpty()) {
//            products = this.productClient.findAllProductsByFilterAndCategoryIds(filter, categoryIdsForFilter);
//        } else {
//            products = this.productClient.findAllProducts(filter, null);
//        }
//
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categories);
//        model.addAttribute("filter", filter);
//        model.addAttribute("filterCategory", filterCategory);
//
//        return "catalogue/products/list";
//    }


    @GetMapping("create_new_product")
    public String getNewProductPage() {
        return "catalogue/products/create_new_product";
    }


    @PostMapping("create_new_product")
    public String createNewProduct(NewProductPayload payload,
                                   Model model) {
        try {
            Product product =
                    this.productClient.createProduct(payload.title(), payload.description());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/create_new_product";
        }
    }


}
