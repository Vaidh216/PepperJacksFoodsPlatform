package com.pepperjacks.foodplatform.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    
    private Long id;
    private Long userId;
    private Long kitchenId;
    private String kitchenName;
    private Long addressId;
    private BigDecimal itemsTotal;
    private BigDecimal deliveryFee;
    private BigDecimal tax;
    private BigDecimal total;
    private String status;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer estimatedDeliveryMinutes;
    private List<OrderItemDto> items;
}

