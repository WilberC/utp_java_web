package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String dashboard(Model model) {
        // Get summary statistics
        Map<String, Object> summaryStats = reportService.getSummaryStatistics();
        model.addAttribute("summaryStats", summaryStats);
        model.addAttribute("title", "Reportes");
        return "admin/reports/dashboard";
    }

    @GetMapping("/sales")
    public String salesReport(Model model,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate) {
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        Map<String, Object> salesData = reportService.getSalesReport(start, end);
        model.addAttribute("salesData", salesData);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        model.addAttribute("title", "Reporte de Ventas");
        
        return "admin/reports/sales";
    }

    @GetMapping("/products")
    public String productReport(Model model) {
        Map<String, Object> productData = reportService.getProductReport();
        model.addAttribute("productData", productData);
        model.addAttribute("title", "Reporte de Productos");
        return "admin/reports/products";
    }

    @GetMapping("/customers")
    public String customerReport(Model model) {
        Map<String, Object> customerData = reportService.getCustomerReport();
        model.addAttribute("customerData", customerData);
        model.addAttribute("title", "Reporte de Clientes");
        return "admin/reports/customers";
    }

    @GetMapping("/inventory")
    public String inventoryReport(Model model) {
        Map<String, Object> inventoryData = reportService.getInventoryReport();
        model.addAttribute("inventoryData", inventoryData);
        model.addAttribute("title", "Reporte de Inventario");
        return "admin/reports/inventory";
    }

    @GetMapping("/export/sales")
    public String exportSalesReport(@RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(defaultValue = "pdf") String format) {
        // This would typically return a file download
        // For now, we'll redirect to the sales report page
        return "redirect:/admin/reports/sales?startDate=" + startDate + "&endDate=" + endDate;
    }

    @GetMapping("/export/products")
    public String exportProductReport(@RequestParam(defaultValue = "pdf") String format) {
        // This would typically return a file download
        // For now, we'll redirect to the product report page
        return "redirect:/admin/reports/products";
    }
} 