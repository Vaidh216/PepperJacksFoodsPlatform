package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.order.OrderDto;
import com.pepperjacks.foodplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
@RequiredArgsConstructor
public class KitchenController {
    
    private final OrderService orderService;
    
    @GetMapping("/{kitchenId}/orders")
    public ResponseEntity<List<OrderDto>> getKitchenOrders(@PathVariable Long kitchenId) {
        List<OrderDto> orders = orderService.getKitchenOrders(kitchenId);
        return ResponseEntity.ok(orders);
    }
    
    @PatchMapping("/orders/{orderId}/accept")
    public ResponseEntity<OrderDto> acceptOrder(@PathVariable Long orderId) {
        OrderDto order = orderService.updateOrderStatus(orderId, "ACCEPTED");
        return ResponseEntity.ok(order);
    }
    
    @PatchMapping("/orders/{orderId}/preparing")
    public ResponseEntity<OrderDto> markPreparing(@PathVariable Long orderId) {
        OrderDto order = orderService.updateOrderStatus(orderId, "PREPARING");
        return ResponseEntity.ok(order);
    }
    
    @PatchMapping("/orders/{orderId}/ready")
    public ResponseEntity<OrderDto> markReady(@PathVariable Long orderId) {
        OrderDto order = orderService.updateOrderStatus(orderId, "READY");
        return ResponseEntity.ok(order);
    }
}

