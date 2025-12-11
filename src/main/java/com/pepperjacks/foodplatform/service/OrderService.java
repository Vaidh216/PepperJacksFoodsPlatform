package com.pepperjacks.foodplatform.service;

import com.pepperjacks.foodplatform.dto.order.CreateOrderRequestDto;
import com.pepperjacks.foodplatform.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    
    OrderDto createOrder(Long userId, CreateOrderRequestDto request);
    
    OrderDto getOrderById(Long orderId, Long userId);
    
    List<OrderDto> getUserOrders(Long userId);
    
    OrderDto updateOrderStatus(Long orderId, String status);
    
    List<OrderDto> getKitchenOrders(Long kitchenId);
}

