package com.pepperjacks.foodplatform.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {
    
    private Long id;
    private Long userId;
    private String fullName;
    private String phone;
    private String vehicleType;
    private String vehicleNumber;
    private Boolean isAvailable;
    private Double currentLatitude;
    private Double currentLongitude;
}

