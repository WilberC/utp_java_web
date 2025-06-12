package com.utp.webdevelopment.service;

import com.utp.webdevelopment.model.Order;
import com.utp.webdevelopment.model.enums.OrderStatus;
import com.utp.webdevelopment.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Page<Order> findAllOrdersPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByStatus(String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid order status: {}", status);
            return List.of();
        }
    }

    public Page<Order> findOrdersByStatus(String status, Pageable pageable) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStatus(orderStatus, pageable);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid order status: {}", status);
            return Page.empty(pageable);
        }
    }

    public List<Order> findOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return orderRepository.findByOrderDateBetween(startDateTime, endDateTime);
    }

    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        log.info("Created order with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(orderDetails.getStatus());
                    existingOrder.setTotalAmount(orderDetails.getTotalAmount());
                    existingOrder.setOrderItems(orderDetails.getOrderItems());
                    
                    Order updatedOrder = orderRepository.save(existingOrder);
                    log.info("Updated order with ID: {}", updatedOrder.getId());
                    return updatedOrder;
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public void updateOrderStatus(Long id, String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            orderRepository.findById(id)
                    .ifPresent(order -> {
                        order.setStatus(orderStatus);
                        orderRepository.save(order);
                        log.info("Updated order status for ID: {} to: {}", id, status);
                    });
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    public void deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            log.info("Deleted order with ID: {}", id);
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public long countOrdersByStatus(String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.countByStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid order status: {}", status);
            return 0;
        }
    }
} 