package com.utp.webdevelopment.controller;

import com.utp.webdevelopment.model.Order;
import com.utp.webdevelopment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(Model model, 
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "orderDate") String sortBy,
                           @RequestParam(defaultValue = "desc") String sortDir,
                           @RequestParam(required = false) String status) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> ordersPage;
        if (status != null && !status.trim().isEmpty()) {
            ordersPage = orderService.findOrdersByStatus(status, pageable);
        } else {
            ordersPage = orderService.findAllOrdersPaginated(pageable);
        }
        
        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalItems", ordersPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("status", status);
        model.addAttribute("title", "Pedidos");
        
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String showOrder(@PathVariable Long id, Model model) {
        return orderService.findOrderById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("title", "Ver Pedido");
                    return "admin/orders/show";
                })
                .orElse("redirect:/admin/orders");
    }

    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Estado del pedido actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error updating order status", e);
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado del pedido: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/pending")
    public String listPendingOrders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("PENDING"));
        model.addAttribute("title", "Pedidos Pendientes");
        return "admin/orders/pending";
    }

    @GetMapping("/processing")
    public String listProcessingOrders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("PROCESSING"));
        model.addAttribute("title", "Pedidos en Proceso");
        return "admin/orders/processing";
    }

    @GetMapping("/shipped")
    public String listShippedOrders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("SHIPPED"));
        model.addAttribute("title", "Pedidos Enviados");
        return "admin/orders/shipped";
    }

    @GetMapping("/delivered")
    public String listDeliveredOrders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("DELIVERED"));
        model.addAttribute("title", "Pedidos Entregados");
        return "admin/orders/delivered";
    }

    @GetMapping("/cancelled")
    public String listCancelledOrders(Model model) {
        model.addAttribute("orders", orderService.findOrdersByStatus("CANCELLED"));
        model.addAttribute("title", "Pedidos Cancelados");
        return "admin/orders/cancelled";
    }
} 