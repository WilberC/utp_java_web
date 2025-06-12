package com.utp.webdevelopment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Size(max = 50)
    @Column(unique = true)
    private String barcode;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Size(max = 20)
    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 10;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    public boolean isFeatured() {
        return this.isFeatured != null && this.isFeatured;
    }

    public String getSku() {
        return this.barcode != null ? this.barcode : "SKU" + String.format("%06d", this.id);
    }
} 