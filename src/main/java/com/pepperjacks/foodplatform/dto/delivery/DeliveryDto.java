package com.pepperjacks.foodplatform.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    
    private Long id;
    private Long orderId;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedAt;
    private LocalDateTime deliveredAt;
    private Integer estimatedDeliveryMinutes;
}

