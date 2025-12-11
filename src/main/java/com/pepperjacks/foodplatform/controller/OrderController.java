package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.order.CreateOrderRequestDto;
import com.pepperjacks.foodplatform.dto.order.OrderDto;
import com.pepperjacks.foodplatform.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequestDto request) {
        OrderDto order = orderService.createOrder(userId, request);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId, userId);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(@RequestHeader("X-User-Id") Long userId) {
        List<OrderDto> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
    
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        OrderDto order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
}

