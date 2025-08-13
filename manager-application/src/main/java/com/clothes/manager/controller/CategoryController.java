package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoryClient;
import com.clothes.manager.controller.payload.UpdateCategoryPayload;
import com.clothes.manager.dto.Category;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/categories/{categoryId:\\d+}")
public class CategoryController {

    private final CategoryClient categoryClient;

    @ModelAttribute("category")
    public Category category(@PathVariable("categoryId") Integer categoryId) {
        return this.categoryClient.findCategoryById(categoryId);
    }

    @GetMapping
    public String getCategoryPage() {
        return "catalogue/categories/category";
    }

    @GetMapping("edit_category")
    public String getCategoryEditPage() {
        return "catalogue/categories/edit_category";
    }

    @PostMapping("edit_category")
    public String updateCategory(
            @ModelAttribute(value = "category", binding = false) Category category,
            UpdateCategoryPayload payload,
            Model model
    ) {
        try {
            this.categoryClient.updateCategory(category.id(), payload.title());
            return "redirect:/catalogue/categories/%d".formatted(category.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/categories/edit_category";
        }
    }

    @PostMapping("delete_category")
    public String deleteCategory(
            @ModelAttribute(value = "category", binding = false) Category category
    ) {
        this.categoryClient.deleteCategory(category.id());
        return "redirect:/catalogue/categories/list";
    }

}
