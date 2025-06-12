package com.utp.webdevelopment.service;

import com.utp.webdevelopment.model.Order;
import com.utp.webdevelopment.model.Product;
import com.utp.webdevelopment.model.User;
import com.utp.webdevelopment.model.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;

    public Map<String, Object> getSummaryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", orderService.countOrders());
        stats.put("totalRevenue", calculateTotalRevenue());
        stats.put("totalCustomers", userService.countUsersByRole(UserRole.CUSTOMER));
        stats.put("totalProducts", productService.countProducts());
        stats.put("totalUsers", userService.countUsers());
        stats.put("totalCategories", categoryService.countCategories());
        stats.put("featuredProducts", productService.countFeaturedProducts());
        stats.put("lowStockProducts", productService.findLowStockProducts().size());
        
        // Order status counts
        stats.put("pendingOrders", orderService.countOrdersByStatus("PENDING"));
        stats.put("processingOrders", orderService.countOrdersByStatus("PROCESSING"));
        stats.put("shippedOrders", orderService.countOrdersByStatus("SHIPPED"));
        stats.put("deliveredOrders", orderService.countOrdersByStatus("DELIVERED"));
        stats.put("cancelledOrders", orderService.countOrdersByStatus("CANCELLED"));
        
        return stats;
    }

    public Map<String, Object> getSalesReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Get orders in date range
        List<Order> orders = orderService.findOrdersByDateRange(startDate, endDate);
        
        double totalSales = orders.stream()
                .filter(order -> !"CANCELLED".equals(order.getStatus().name()))
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
        
        int totalOrders = orders.size();
        double averageOrderValue = totalOrders > 0 ? totalSales / totalOrders : 0.0;
        
        // Top products
        Map<String, Object> topProducts = getTopSellingProducts(orders);
        
        // Sales by day
        Map<String, Object> salesByDay = getSalesByDay(orders);
        
        // Detailed orders
        List<Map<String, Object>> orderDetails = orders.stream()
                .map(this::convertOrderToMap)
                .collect(Collectors.toList());
        
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("totalSales", totalSales);
        report.put("totalOrders", totalOrders);
        report.put("averageOrderValue", averageOrderValue);
        report.put("topProducts", topProducts);
        report.put("salesByDay", salesByDay);
        report.put("orders", orderDetails);
        
        log.info("Generated sales report for period: {} to {}", startDate, endDate);
        return report;
    }

    public Map<String, Object> getProductReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<Product> allProducts = productService.findAllProducts();
        List<Product> lowStockProducts = productService.findLowStockProducts();
        List<Product> outOfStockProducts = productService.findOutOfStockProducts();
        
        report.put("totalProducts", allProducts.size());
        report.put("featuredProducts", productService.countFeaturedProducts());
        report.put("lowStockProducts", lowStockProducts.size());
        report.put("outOfStockProducts", outOfStockProducts.size());
        
        // Products by category
        Map<String, Object> productsByCategory = getProductsByCategory(allProducts);
        
        // Top selling products
        Map<String, Object> topSellingProducts = getTopSellingProductsFromOrders();
        
        // Out of stock products
        List<Map<String, Object>> outOfStockList = outOfStockProducts.stream()
                .map(this::convertProductToMap)
                .collect(Collectors.toList());
        
        // Low stock products
        List<Map<String, Object>> lowStockList = lowStockProducts.stream()
                .map(this::convertProductToMap)
                .collect(Collectors.toList());
        
        report.put("productsByCategory", productsByCategory);
        report.put("topSellingProducts", topSellingProducts);
        report.put("outOfStockProducts", outOfStockList);
        report.put("lowStockProducts", lowStockList);
        
        log.info("Generated product report");
        return report;
    }

    public Map<String, Object> getCustomerReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<User> allUsers = userService.findAllUsers();
        List<User> customers = userService.findUsersByRole(UserRole.CUSTOMER);
        List<User> admins = userService.findUsersByRole(UserRole.ADMIN);
        
        report.put("totalCustomers", customers.size());
        report.put("totalAdmins", admins.size());
        report.put("activeCustomers", calculateActiveCustomers(customers));
        report.put("newCustomersThisMonth", calculateNewCustomersThisMonth(customers));
        
        // Top customers
        Map<String, Object> topCustomers = getTopCustomers(customers);
        
        // Customer growth
        Map<String, Object> customerGrowth = getCustomerGrowth();
        
        // Customer segments
        report.put("vipCustomers", calculateVipCustomers(customers));
        report.put("vipCustomersList", getVipCustomersList(customers));
        report.put("activeCustomersList", getActiveCustomersList(customers));
        report.put("inactiveCustomers", calculateInactiveCustomers(customers));
        report.put("inactiveCustomersList", getInactiveCustomersList(customers));
        
        report.put("topCustomers", topCustomers);
        report.put("customerGrowth", customerGrowth);
        
        log.info("Generated customer report");
        return report;
    }

    public Map<String, Object> getInventoryReport() {
        Map<String, Object> report = new HashMap<>();
        
        List<Product> allProducts = productService.findAllProducts();
        List<Product> lowStockProducts = productService.findLowStockProducts();
        List<Product> outOfStockProducts = productService.findOutOfStockProducts();
        
        double totalInventoryValue = allProducts.stream()
                .mapToDouble(product -> product.getPrice().doubleValue() * product.getStock())
                .sum();
        
        report.put("totalProducts", allProducts.size());
        report.put("lowStockProducts", lowStockProducts.size());
        report.put("outOfStockProducts", outOfStockProducts.size());
        report.put("totalInventoryValue", totalInventoryValue);
        
        // Inventory by category
        Map<String, Object> inventoryByCategory = getInventoryByCategory(allProducts);
        
        // Stock alerts
        List<Map<String, Object>> stockAlerts = lowStockProducts.stream()
                .map(this::convertProductToStockAlert)
                .collect(Collectors.toList());
        
        // Out of stock products
        List<Map<String, Object>> outOfStockList = outOfStockProducts.stream()
                .map(this::convertProductToMap)
                .collect(Collectors.toList());
        
        // High value products
        List<Map<String, Object>> highValueProducts = getHighValueProducts(allProducts);
        
        report.put("inventoryByCategory", inventoryByCategory);
        report.put("stockAlerts", stockAlerts);
        report.put("outOfStockProducts", outOfStockList);
        report.put("highValueProducts", highValueProducts);
        
        log.info("Generated inventory report");
        return report;
    }

    // Helper methods
    private double calculateTotalRevenue() {
        List<Order> allOrders = orderService.findAllOrders();
        return allOrders.stream()
                .filter(order -> !"CANCELLED".equals(order.getStatus().name()))
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
    }

    private Map<String, Object> getTopSellingProducts(List<Order> orders) {
        Map<String, Object> result = new HashMap<>();
        
        // Simulate top products data
        List<Map<String, Object>> topProducts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("name", "Producto " + (i + 1));
            product.put("quantity", 100 - (i * 15));
            product.put("revenue", 5000.0 - (i * 500));
            topProducts.add(product);
        }
        
        result.put("products", topProducts);
        return result;
    }

    private Map<String, Object> getSalesByDay(List<Order> orders) {
        Map<String, Object> result = new HashMap<>();
        
        // Simulate sales by day data
        LocalDate currentDate = LocalDate.now().minusDays(30);
        for (int i = 0; i < 30; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", currentDate.plusDays(i));
            dayData.put("orders", (int) (Math.random() * 10) + 1);
            dayData.put("sales", (Math.random() * 1000) + 100);
            result.put("day_" + i, dayData);
        }
        
        return result;
    }

    private Map<String, Object> convertOrderToMap(Order order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.getId());
        orderMap.put("customerName", order.getUser().getName());
        orderMap.put("date", order.getCreatedAt());
        orderMap.put("status", order.getStatus().name());
        orderMap.put("itemCount", order.getOrderItems().size());
        orderMap.put("total", order.getTotalAmount().doubleValue());
        return orderMap;
    }

    private Map<String, Object> getProductsByCategory(List<Product> products) {
        Map<String, Object> result = new HashMap<>();
        
        // Group products by category
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(product -> 
                    product.getCategory() != null ? product.getCategory().getName() : "Sin Categoría"));
        
        productsByCategory.forEach((categoryName, productList) -> {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("name", categoryName);
            categoryData.put("totalProducts", productList.size());
            categoryData.put("activeProducts", productList.stream().filter(Product::isActive).count());
            categoryData.put("featuredProducts", productList.stream().filter(Product::isFeatured).count());
            categoryData.put("lowStockProducts", productList.stream().filter(p -> p.getStock() <= 10).count());
            categoryData.put("outOfStockProducts", productList.stream().filter(p -> p.getStock() == 0).count());
            categoryData.put("totalValue", productList.stream().mapToDouble(p -> p.getPrice().doubleValue() * p.getStock()).sum());
            result.put(categoryName, categoryData);
        });
        
        return result;
    }

    private Map<String, Object> getTopSellingProductsFromOrders() {
        Map<String, Object> result = new HashMap<>();
        
        // Simulate top selling products
        List<Map<String, Object>> topProducts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> product = new HashMap<>();
            product.put("name", "Producto Destacado " + (i + 1));
            product.put("category", "Categoría " + ((i % 3) + 1));
            product.put("price", 100.0 + (i * 50));
            product.put("stock", 50 - (i * 3));
            product.put("salesCount", 200 - (i * 15));
            product.put("revenue", 20000.0 - (i * 1500));
            product.put("imageUrl", "/images/product" + (i + 1) + ".jpg");
            product.put("sku", "SKU" + String.format("%03d", i + 1));
            topProducts.add(product);
        }
        
        result.put("products", topProducts);
        return result;
    }

    private Map<String, Object> convertProductToMap(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        productMap.put("category", product.getCategory() != null ? product.getCategory().getName() : "Sin Categoría");
        productMap.put("price", product.getPrice().doubleValue());
        productMap.put("stock", product.getStock());
        productMap.put("imageUrl", product.getImageUrl());
        productMap.put("sku", product.getSku());
        productMap.put("lastSale", LocalDate.now().minusDays((int) (Math.random() * 30)));
        productMap.put("value", product.getPrice().doubleValue() * product.getStock());
        return productMap;
    }

    private Map<String, Object> convertProductToStockAlert(Product product) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("id", product.getId());
        alert.put("name", product.getName());
        alert.put("category", product.getCategory() != null ? product.getCategory().getName() : "Sin Categoría");
        alert.put("currentStock", product.getStock());
        alert.put("minStock", 10);
        alert.put("value", product.getPrice().doubleValue() * product.getStock());
        alert.put("imageUrl", product.getImageUrl());
        alert.put("sku", product.getSku());
        return alert;
    }

    private int calculateActiveCustomers(List<User> customers) {
        // Simulate active customers (customers with orders in last 30 days)
        return (int) (customers.size() * 0.7);
    }

    private int calculateNewCustomersThisMonth(List<User> customers) {
        // Simulate new customers this month
        return (int) (customers.size() * 0.1);
    }

    private Map<String, Object> getTopCustomers(List<User> customers) {
        Map<String, Object> result = new HashMap<>();
        
        // Simulate top customers
        List<Map<String, Object>> topCustomers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("id", i + 1L);
            customer.put("name", "Cliente " + (i + 1));
            customer.put("email", "cliente" + (i + 1) + "@example.com");
            customer.put("phone", "+51 999 999 " + String.format("%03d", i + 1));
            customer.put("totalOrders", 50 - (i * 3));
            customer.put("totalSpent", 5000.0 - (i * 300));
            customer.put("averageOrderValue", 100.0 - (i * 5));
            customer.put("lastOrderDate", LocalDate.now().minusDays(i * 2));
            customer.put("status", i < 7 ? "ACTIVE" : "INACTIVE");
            topCustomers.add(customer);
        }
        
        result.put("customers", topCustomers);
        return result;
    }

    private Map<String, Object> getCustomerGrowth() {
        Map<String, Object> result = new HashMap<>();
        
        // Simulate customer growth data
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"};
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", months[i] + " 2024");
            monthData.put("newCustomers", 20 + (i * 5));
            monthData.put("activeCustomers", 150 + (i * 10));
            monthData.put("totalOrders", 300 + (i * 20));
            monthData.put("revenue", 15000.0 + (i * 1000));
            monthData.put("growth", 5 + (i * 2));
            result.put("month_" + i, monthData);
        }
        
        return result;
    }

    private int calculateVipCustomers(List<User> customers) {
        // Simulate VIP customers (customers with > S/ 1000 spent)
        return (int) (customers.size() * 0.15);
    }

    private List<Map<String, Object>> getVipCustomersList(List<User> customers) {
        List<Map<String, Object>> vipCustomers = new ArrayList<>();
        
        // Simulate VIP customers list
        for (int i = 0; i < 5; i++) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("name", "Cliente VIP " + (i + 1));
            customer.put("email", "vip" + (i + 1) + "@example.com");
            customer.put("totalSpent", 2000.0 + (i * 500));
            vipCustomers.add(customer);
        }
        
        return vipCustomers;
    }

    private List<Map<String, Object>> getActiveCustomersList(List<User> customers) {
        List<Map<String, Object>> activeCustomers = new ArrayList<>();
        
        // Simulate active customers list
        for (int i = 0; i < 5; i++) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("name", "Cliente Activo " + (i + 1));
            customer.put("lastOrderDate", LocalDate.now().minusDays(i + 1));
            customer.put("totalOrders", 10 + i);
            activeCustomers.add(customer);
        }
        
        return activeCustomers;
    }

    private int calculateInactiveCustomers(List<User> customers) {
        // Simulate inactive customers
        return (int) (customers.size() * 0.25);
    }

    private List<Map<String, Object>> getInactiveCustomersList(List<User> customers) {
        List<Map<String, Object>> inactiveCustomers = new ArrayList<>();
        
        // Simulate inactive customers list
        for (int i = 0; i < 5; i++) {
            Map<String, Object> customer = new HashMap<>();
            customer.put("name", "Cliente Inactivo " + (i + 1));
            customer.put("lastOrderDate", LocalDate.now().minusDays(90 + i * 10));
            customer.put("daysInactive", 90 + i * 10);
            inactiveCustomers.add(customer);
        }
        
        return inactiveCustomers;
    }

    private Map<String, Object> getInventoryByCategory(List<Product> products) {
        Map<String, Object> result = new HashMap<>();
        
        // Group products by category
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(product -> 
                    product.getCategory() != null ? product.getCategory().getName() : "Sin Categoría"));
        
        productsByCategory.forEach((categoryName, productList) -> {
            Map<String, Object> categoryData = new HashMap<>();
            categoryData.put("id", productList.get(0).getCategory() != null ? productList.get(0).getCategory().getId() : 0L);
            categoryData.put("name", categoryName);
            categoryData.put("totalProducts", productList.size());
            categoryData.put("totalStock", productList.stream().mapToInt(Product::getStock).sum());
            categoryData.put("totalValue", productList.stream().mapToDouble(p -> p.getPrice().doubleValue() * p.getStock()).sum());
            categoryData.put("lowStockProducts", productList.stream().filter(p -> p.getStock() <= 10).count());
            categoryData.put("outOfStockProducts", productList.stream().filter(p -> p.getStock() == 0).count());
            categoryData.put("averageValue", productList.stream().mapToDouble(p -> p.getPrice().doubleValue()).average().orElse(0.0));
            result.put(categoryName, categoryData);
        });
        
        return result;
    }

    private List<Map<String, Object>> getHighValueProducts(List<Product> products) {
        return products.stream()
                .filter(product -> product.getPrice().doubleValue() > 500) // High value threshold
                .limit(10)
                .map(product -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", product.getId());
                    productMap.put("name", product.getName());
                    productMap.put("category", product.getCategory() != null ? product.getCategory().getName() : "Sin Categoría");
                    productMap.put("price", product.getPrice().doubleValue());
                    productMap.put("stock", product.getStock());
                    productMap.put("stockValue", product.getPrice().doubleValue() * product.getStock());
                    productMap.put("imageUrl", product.getImageUrl());
                    productMap.put("sku", product.getSku());
                    return productMap;
                })
                .collect(Collectors.toList());
    }
} 