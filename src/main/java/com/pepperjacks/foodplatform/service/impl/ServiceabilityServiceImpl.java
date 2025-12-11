package com.pepperjacks.foodplatform.service.impl;

import com.pepperjacks.foodplatform.dto.kitchen.KitchenDto;
import com.pepperjacks.foodplatform.dto.serviceability.ServiceabilityResponseDto;
import com.pepperjacks.foodplatform.model.KitchenEntity;
import com.pepperjacks.foodplatform.repository.KitchenRepository;
import com.pepperjacks.foodplatform.service.ServiceabilityService;
import com.pepperjacks.foodplatform.util.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceabilityServiceImpl implements ServiceabilityService {
    
    private final KitchenRepository kitchenRepository;
    private final DistanceCalculator distanceCalculator;
    
    private static final double MAX_SERVICEABLE_DISTANCE_KM = 3.0;
    
    @Override
    public ServiceabilityResponseDto checkServiceability(Double latitude, Double longitude) {
        List<KitchenEntity> activeKitchens = kitchenRepository.findByIsActiveTrue();
        
        KitchenEntity nearestKitchen = null;
        double minDistance = Double.MAX_VALUE;
        
        for (KitchenEntity kitchen : activeKitchens) {
            double distance = distanceCalculator.calculateDistance(
                    latitude, longitude,
                    kitchen.getLatitude().doubleValue(), kitchen.getLongitude().doubleValue()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
                nearestKitchen = kitchen;
            }
        }
        
        if (nearestKitchen == null) {
            return ServiceabilityResponseDto.builder()
                    .isServiceable(false)
                    .message("No kitchens available")
                    .build();
        }
        
        boolean isServiceable = minDistance <= MAX_SERVICEABLE_DISTANCE_KM;
        int estimatedDeliveryMinutes = distanceCalculator.calculateEstimatedDeliveryMinutes(minDistance);
        
        return ServiceabilityResponseDto.builder()
                .isServiceable(isServiceable)
                .message(isServiceable ? "Service available" : "Out of service area")
                .nearestKitchen(mapToKitchenDto(nearestKitchen, minDistance))
                .distanceKm(minDistance)
                .estimatedDeliveryMinutes(estimatedDeliveryMinutes)
                .build();
    }
    
    private KitchenDto mapToKitchenDto(KitchenEntity kitchen, double distance) {
        return KitchenDto.builder()
                .id(kitchen.getId())
                .name(kitchen.getName())
                .description(null) // Kitchen entity doesn't have description field
                .address(kitchen.getAddress())
                .latitude(kitchen.getLatitude().doubleValue())
                .longitude(kitchen.getLongitude().doubleValue())
                .isActive(kitchen.getIsActive())
                .phone(kitchen.getPhone())
                .imageUrl(null) // Kitchen entity doesn't have imageUrl field
                .distanceKm(distance)
                .build();
    }
}
