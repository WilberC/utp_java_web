package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.model.Category;
import com.utp.webdevelopment.service.CategoryService;
import com.utp.webdevelopment.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("activeCategories", categoryService.countCategories());
        model.addAttribute("title", "Categorías");
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("existingCategories", categoryService.findAllCategories());
        model.addAttribute("title", "Crear Categoría");
        return "admin/categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute Category category, 
                               BindingResult result, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("existingCategories", categoryService.findAllCategories());
            model.addAttribute("title", "Crear Categoría");
            return "admin/categories/create";
        }
        
        try {
            if (categoryService.existsByName(category.getName())) {
                result.rejectValue("name", "error.category", "La categoría ya existe");
                model.addAttribute("existingCategories", categoryService.findAllCategories());
                model.addAttribute("title", "Crear Categoría");
                return "admin/categories/create";
            }
            
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("success", "Categoría creada exitosamente");
            return "redirect:/admin/categories";
            
        } catch (Exception e) {
            log.error("Error creating category", e);
            model.addAttribute("error", "Error al crear la categoría: " + e.getMessage());
            model.addAttribute("existingCategories", categoryService.findAllCategories());
            model.addAttribute("title", "Crear Categoría");
            return "admin/categories/create";
        }
    }

    @GetMapping("/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        return categoryService.findCategoryById(id)
                .map(category -> {
                    model.addAttribute("category", category);
                    model.addAttribute("title", "Ver Categoría");
                    return "admin/categories/show";
                })
                .orElse("redirect:/admin/categories");
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return categoryService.findCategoryById(id)
                .map(category -> {
                    model.addAttribute("category", category);
                    model.addAttribute("title", "Editar Categoría");
                    return "admin/categories/edit";
                })
                .orElse("redirect:/admin/categories");
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                               @Valid @ModelAttribute Category category,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("title", "Editar Categoría");
            return "admin/categories/edit";
        }
        
        try {
            // Check if name is already taken by another category
            categoryService.findCategoryByName(category.getName())
                    .ifPresent(existingCategory -> {
                        if (!existingCategory.getId().equals(id)) {
                            result.rejectValue("name", "error.category", "La categoría ya existe");
                        }
                    });
            
            if (result.hasErrors()) {
                model.addAttribute("title", "Editar Categoría");
                return "admin/categories/edit";
            }
            
            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada exitosamente");
            return "redirect:/admin/categories";
            
        } catch (Exception e) {
            log.error("Error updating category", e);
            model.addAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
            model.addAttribute("title", "Editar Categoría");
            return "admin/categories/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error deleting category", e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
} 