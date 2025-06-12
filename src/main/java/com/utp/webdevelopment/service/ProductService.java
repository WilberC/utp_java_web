package com.utp.webdevelopment.service;

import com.utp.webdevelopment.model.Product;
import com.utp.webdevelopment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> findAllProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    public List<Product> findProductsByStatus(String status) {
        return productRepository.findByStatus(status);
    }

    public List<Product> findLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public Product createProduct(Product product) {
        // Set default values if not provided
        if (product.getStatus() == null) {
            product.setStatus("ACTIVE");
        }
        if (product.getLowStockThreshold() == null) {
            product.setLowStockThreshold(10);
        }
        if (product.getIsFeatured() == null) {
            product.setIsFeatured(false);
        }
        
        Product savedProduct = productRepository.save(product);
        log.info("Created product with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setDescription(productDetails.getDescription());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setStock(productDetails.getStock());
                    existingProduct.setCategory(productDetails.getCategory());
                    existingProduct.setBarcode(productDetails.getBarcode());
                    existingProduct.setImageUrl(productDetails.getImageUrl());
                    existingProduct.setStatus(productDetails.getStatus());
                    existingProduct.setLowStockThreshold(productDetails.getLowStockThreshold());
                    existingProduct.setIsFeatured(productDetails.getIsFeatured());
                    
                    Product updatedProduct = productRepository.save(existingProduct);
                    log.info("Updated product with ID: {}", updatedProduct.getId());
                    return updatedProduct;
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            log.info("Deleted product with ID: {}", id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    public long countProducts() {
        return productRepository.count();
    }

    public long countProductsByCategory(Long categoryId) {
        return productRepository.countByCategoryId(categoryId);
    }

    public long countFeaturedProducts() {
        return productRepository.countByIsFeaturedTrue();
    }

    public void updateStock(Long productId, Integer newStock) {
        productRepository.findById(productId)
                .ifPresent(product -> {
                    product.setStock(newStock);
                    productRepository.save(product);
                    log.info("Updated stock for product ID: {} to: {}", productId, newStock);
                });
    }

    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchProducts(searchTerm);
    }

    public Page<Product> findProductsByCategoryIdPaginated(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> searchProductsPaginated(String searchTerm, Pageable pageable) {
        return productRepository.searchProductsPaginated(searchTerm, pageable);
    }

    public Page<Product> findActiveProductsPaginated(Pageable pageable) {
        return productRepository.findByStatus("ACTIVE", pageable);
    }
} 