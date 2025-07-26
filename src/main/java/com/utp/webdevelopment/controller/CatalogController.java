package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.model.Product;
import com.utp.webdevelopment.service.ProductService;
import com.utp.webdevelopment.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/catalog")
    public String catalog(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "12") int size,
                         @RequestParam(defaultValue = "name") String sortBy,
                         @RequestParam(defaultValue = "asc") String sortDir,
                         @RequestParam(required = false) String search,
                         @RequestParam(required = false) Long categoryId) {
        
        log.info("Catalog called with page={}, size={}, sortBy={}, sortDir={}, search={}, categoryId={}", 
                page, size, sortBy, sortDir, search, categoryId);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> productsPage;
        if (search != null && !search.trim().isEmpty()) {
            productsPage = productService.searchProductsPaginated(search.trim(), pageable);
        } else if (categoryId != null) {
            productsPage = productService.findProductsByCategoryIdPaginated(categoryId, pageable);
        } else {
            productsPage = productService.findActiveProductsPaginated(pageable);
        }
        
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "Catálogo de Productos");
        
        return "catalog/index";
    }

    @GetMapping("/catalog/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        return productService.findProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("relatedProducts", productService.findProductsByCategoryId(product.getCategory().getId()));
                    model.addAttribute("title", product.getName());
                    return "catalog/product-detail";
                })
                .orElse("redirect:/catalog");
    }

    @GetMapping("/catalog/featured")
    public String featuredProducts(Model model) {
        model.addAttribute("products", productService.findFeaturedProducts());
        model.addAttribute("title", "Productos Destacados");
        return "catalog/featured";
    }

    @GetMapping("/catalog/category/{categoryId}")
    public String productsByCategory(@PathVariable Long categoryId,
                                   Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "12") int size) {
        
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> productsPage = productService.findProductsByCategoryIdPaginated(categoryId, pageable);
        
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("currentCategory", categoryService.findCategoryById(categoryId).orElse(null));
        model.addAttribute("title", "Productos por Categoría");
        
        return "catalog/category";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredProducts", productService.findFeaturedProducts());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "Inicio");
        return "home";
    }
} 