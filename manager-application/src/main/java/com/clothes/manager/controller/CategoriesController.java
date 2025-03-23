package com.clothes.manager.controller;

import com.clothes.manager.client.general.CategoryClient;
import com.clothes.manager.controller.payload.NewCategoryPayload;
import com.clothes.manager.dto.Category;
import com.clothes.manager.exception.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/categories")
public class CategoriesController {

    private final CategoryClient categoryClient;

    @GetMapping("list")
    public String getRootCategories(Model model) {
        model.addAttribute("categories", categoryClient.getRootCategories());
        return "catalogue/categories/list";
    }

    @GetMapping("create_new_category")
    public String getNewCategoryPage() {
        return "catalogue/categories/create_new_category";
    }

    @PostMapping("create_new_category")
    public String createNewCategory(
            NewCategoryPayload payload,
            Model model) {
        try {
            Category category =
                    this.categoryClient.createCategory(payload.title(), payload.parentTitle());
            return "redirect:/catalogue/categories/%d".formatted(category.id());
        } catch (BadRequestException exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/categories/create_new_category";
        }
    }
}
