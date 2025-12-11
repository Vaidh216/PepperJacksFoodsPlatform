package com.pepperjacks.foodplatform.dto.serviceability;

import com.pepperjacks.foodplatform.dto.kitchen.KitchenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceabilityResponseDto {
    
    private Boolean isServiceable;
    private String message;
    private KitchenDto nearestKitchen;
    private Double distanceKm;
    private Integer estimatedDeliveryMinutes;
}

