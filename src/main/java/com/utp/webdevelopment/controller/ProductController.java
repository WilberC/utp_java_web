package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.model.Product;
import com.utp.webdevelopment.service.ProductService;
import com.utp.webdevelopment.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model, 
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(defaultValue = "id") String sortBy,
                             @RequestParam(defaultValue = "asc") String sortDir,
                             @RequestParam(required = false) String search,
                             @RequestParam(required = false) Long categoryId) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Product> productsPage;
        if (search != null && !search.trim().isEmpty()) {
            // For now, we'll show all products. In a real app, you'd implement search
            productsPage = productService.findAllProductsPaginated(pageable);
        } else if (categoryId != null) {
            // Filter by category
            productsPage = productService.findAllProductsPaginated(pageable);
        } else {
            productsPage = productService.findAllProductsPaginated(pageable);
        }
        
        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("search", search);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "Gestión de Productos");
        
        return "admin/products/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("title", "Crear Producto");
        return "admin/products/create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute Product product, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("title", "Crear Producto");
            return "admin/products/create";
        }
        
        try {
            if (product.getBarcode() != null && !product.getBarcode().trim().isEmpty() && 
                productService.existsByBarcode(product.getBarcode())) {
                result.rejectValue("barcode", "error.product", "El código de barras ya existe");
                model.addAttribute("categories", categoryService.findAllCategories());
                model.addAttribute("title", "Crear Producto");
                return "admin/products/create";
            }
            
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Producto creado exitosamente");
            return "redirect:/admin/products";
            
        } catch (Exception e) {
            log.error("Error creating product", e);
            model.addAttribute("error", "Error al crear el producto: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("title", "Crear Producto");
            return "admin/products/create";
        }
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Long id, Model model) {
        return productService.findProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("title", "Detalles del Producto");
                    return "admin/products/show";
                })
                .orElse("redirect:/admin/products");
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        return productService.findProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryService.findAllCategories());
                    model.addAttribute("title", "Editar Producto");
                    return "admin/products/edit";
                })
                .orElse("redirect:/admin/products");
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                              @Valid @ModelAttribute Product product,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("title", "Editar Producto");
            return "admin/products/edit";
        }
        
        try {
            // Check if barcode is already taken by another product
            if (product.getBarcode() != null && !product.getBarcode().trim().isEmpty()) {
                productService.findProductById(id)
                        .ifPresent(existingProduct -> {
                            if (!existingProduct.getBarcode().equals(product.getBarcode()) && 
                                productService.existsByBarcode(product.getBarcode())) {
                                result.rejectValue("barcode", "error.product", "El código de barras ya existe");
                            }
                        });
            }
            
            if (result.hasErrors()) {
                model.addAttribute("categories", categoryService.findAllCategories());
                model.addAttribute("title", "Editar Producto");
                return "admin/products/edit";
            }
            
            productService.updateProduct(id, product);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
            return "redirect:/admin/products";
            
        } catch (Exception e) {
            log.error("Error updating product", e);
            model.addAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAllCategories());
            model.addAttribute("title", "Editar Producto");
            return "admin/products/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error deleting product", e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/featured")
    public String listFeaturedProducts(Model model) {
        model.addAttribute("products", productService.findFeaturedProducts());
        model.addAttribute("title", "Productos Destacados");
        return "admin/products/featured";
    }

    @GetMapping("/low-stock")
    public String listLowStockProducts(Model model) {
        model.addAttribute("products", productService.findLowStockProducts());
        model.addAttribute("title", "Productos con Stock Bajo");
        return "admin/products/low-stock";
    }

    @PostMapping("/{id}/toggle-featured")
    public String toggleFeatured(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.findProductById(id).ifPresent(product -> {
                product.setIsFeatured(!product.getIsFeatured());
                productService.updateProduct(id, product);
                String status = product.getIsFeatured() ? "destacado" : "removido de destacados";
                redirectAttributes.addFlashAttribute("success", "Producto " + status + " exitosamente");
            });
        } catch (Exception e) {
            log.error("Error toggling featured status", e);
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado destacado del producto");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/stats")
    public String showProductStats(Model model) {
        long totalProducts = productService.countProducts();
        long featuredProducts = productService.countFeaturedProducts();
        long lowStockProducts = productService.findLowStockProducts().size();
        long outOfStockProducts = productService.findProductsByStatus("OUT_OF_STOCK").size();
        
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("outOfStockProducts", outOfStockProducts);
        model.addAttribute("title", "Estadísticas de Productos");
        
        return "admin/products/stats";
    }
} 