package com.pepperjacks.foodplatform.dto.kitchen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KitchenDto {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isActive;
    private String phone;
    private String imageUrl;
    private Double distanceKm;
}

