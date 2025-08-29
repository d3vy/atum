package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoriesClient;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("catalogue/categories")
public class CategoriesController {

    private final CategoriesClient categoriesClient;

    public CategoriesController(CategoriesClient categoriesClient) {
        this.categoriesClient = categoriesClient;
    }

    @GetMapping("list")
    public String getAllCategories(
            Model model,
            @RequestParam(value = "filter", required = false) String filter
    ) {
        model.addAttribute("categories", this.categoriesClient.getAllCategories(filter));
        model.addAttribute("filter", filter);
        return "catalogue/categories/list";
    }

    @GetMapping("create_new_category")
    public String getNewCategoryPage() {
        return "catalogue/categories/create_new_category";
    }

    @PostMapping("create_new_category")
    public String createNewCategory(
            Model model,
            @ModelAttribute NewCategoryPayload payload
    ) {
        try {
            var categoryResponse = this.categoriesClient.createCategory(payload);
            model.addAttribute("category", categoryResponse);
            return "redirect:/catalogue/categories/%s".formatted(categoryResponse.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/categories/create_new_category";
        }
    }
}
