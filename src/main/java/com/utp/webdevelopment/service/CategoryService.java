package com.utp.webdevelopment.service;

import com.utp.webdevelopment.model.Category;
import com.utp.webdevelopment.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category createCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        log.info("Created category with ID: {}", savedCategory.getId());
        return savedCategory;
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(categoryDetails.getName());
                    Category updatedCategory = categoryRepository.save(existingCategory);
                    log.info("Updated category with ID: {}", updatedCategory.getId());
                    return updatedCategory;
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            log.info("Deleted category with ID: {}", id);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public long countCategories() {
        return categoryRepository.count();
    }
} 