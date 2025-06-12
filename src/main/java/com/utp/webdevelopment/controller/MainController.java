package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.service.UserService;
import com.utp.webdevelopment.service.ProductService;
import com.utp.webdevelopment.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        // Get statistics
        long totalUsers = userService.countUsers();
        long totalProducts = productService.countProducts();
        long totalCategories = categoryService.countCategories();
        long featuredProducts = productService.countFeaturedProducts();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("title", "Dashboard");
        
        return "admin/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
} 