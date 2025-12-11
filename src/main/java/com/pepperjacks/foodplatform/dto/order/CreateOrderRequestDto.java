package com.pepperjacks.foodplatform.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    
    @NotNull(message = "Kitchen ID is required")
    private Long kitchenId;
    
    @NotNull(message = "Delivery address ID is required")
    private Long addressId;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequestDto> items;
    
    private String specialInstructions;
}

