package com.utp.webdevelopment.repository;

import com.utp.webdevelopment.model.Order;
import com.utp.webdevelopment.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    long countByStatus(OrderStatus status);
    List<Order> findByUserId(Long userId);
    List<Order> findByOrderDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
} 