package com.pepperjacks.foodplatform.service.impl;

import com.pepperjacks.foodplatform.dto.order.*;
import com.pepperjacks.foodplatform.model.*;
import com.pepperjacks.foodplatform.model.OrderEntity.OrderStatus;
import com.pepperjacks.foodplatform.repository.*;
import com.pepperjacks.foodplatform.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final KitchenRepository kitchenRepository;
    private final AddressRepository addressRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    
    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        KitchenEntity kitchen = kitchenRepository.findById(request.getKitchenId())
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));
        
        AddressEntity address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        // Calculate totals
        BigDecimal itemsTotal = BigDecimal.ZERO;
        
        for (OrderItemRequestDto itemRequest : request.getItems()) {
            MenuItemEntity menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            BigDecimal itemTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            itemsTotal = itemsTotal.add(itemTotal);
        }
        
        BigDecimal deliveryFee = BigDecimal.valueOf(30.0);
        BigDecimal tax = itemsTotal.multiply(BigDecimal.valueOf(0.05)); // 5% tax
        BigDecimal total = itemsTotal.add(deliveryFee).add(tax);
        
        // Create order
        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setKitchenId(request.getKitchenId());
        order.setDeliveryAddressId(request.getAddressId());
        order.setTotalAmount(total);
        order.setDeliveryFee(deliveryFee);
        order.setTaxAmount(tax);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.CREATED);
        order.setSpecialInstructions(request.getSpecialInstructions());
        orderRepository.save(order);
        
        // Create order items
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (OrderItemRequestDto itemRequest : request.getItems()) {
            MenuItemEntity menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(order.getId());
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(menuItem.getPrice());
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        
        // Create status history
        createStatusHistory(order.getId(), OrderStatus.CREATED);
        
        return mapToOrderDto(order, orderItems, kitchen.getName());
    }
    
    @Override
    public OrderDto getOrderById(Long orderId, Long userId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }
        
        List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
        KitchenEntity kitchen = kitchenRepository.findById(order.getKitchenId())
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));
        
        return mapToOrderDto(order, items, kitchen.getName());
    }
    
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<OrderEntity> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        
        return orders.stream()
                .map(order -> {
                    List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
                    KitchenEntity kitchen = kitchenRepository.findById(order.getKitchenId())
                            .orElseThrow(() -> new RuntimeException("Kitchen not found"));
                    return mapToOrderDto(order, items, kitchen.getName());
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String statusString) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        OrderStatus status = OrderStatus.valueOf(statusString.toUpperCase());
        order.setStatus(status);
        orderRepository.save(order);
        
        createStatusHistory(order.getId(), status);
        
        List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
        KitchenEntity kitchen = kitchenRepository.findById(order.getKitchenId())
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));
        
        return mapToOrderDto(order, items, kitchen.getName());
    }
    
    @Override
    public List<OrderDto> getKitchenOrders(Long kitchenId) {
        KitchenEntity kitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));
        
        List<OrderEntity> orders = orderRepository.findByKitchenIdOrderByCreatedAtDesc(kitchenId);
        
        return orders.stream()
                .map(order -> {
                    List<OrderItemEntity> items = orderItemRepository.findByOrderId(order.getId());
                    return mapToOrderDto(order, items, kitchen.getName());
                })
                .collect(Collectors.toList());
    }
    
    private void createStatusHistory(Long orderId, OrderStatus status) {
        OrderStatusHistoryEntity history = new OrderStatusHistoryEntity();
        history.setOrderId(orderId);
        history.setStatus(status.name());
        orderStatusHistoryRepository.save(history);
    }
    
    private OrderDto mapToOrderDto(OrderEntity order, List<OrderItemEntity> items, String kitchenName) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .kitchenId(order.getKitchenId())
                .kitchenName(kitchenName)
                .addressId(order.getDeliveryAddressId())
                .itemsTotal(order.getTotalAmount().subtract(order.getDeliveryFee()).subtract(order.getTaxAmount()))
                .deliveryFee(order.getDeliveryFee())
                .tax(order.getTaxAmount())
                .total(order.getTotalAmount())
                .status(order.getStatus().name())
                .specialInstructions(order.getSpecialInstructions())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .estimatedDeliveryMinutes(order.getEstimatedPrepTime() != null ? order.getEstimatedPrepTime() : 35)
                .items(items.stream().map(this::mapToOrderItemDto).collect(Collectors.toList()))
                .build();
    }
    
    private OrderItemDto mapToOrderItemDto(OrderItemEntity item) {
        MenuItemEntity menuItem = menuItemRepository.findById(item.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        
        return OrderItemDto.builder()
                .id(item.getId())
                .menuItemId(item.getMenuItemId())
                .menuItemName(menuItem.getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
