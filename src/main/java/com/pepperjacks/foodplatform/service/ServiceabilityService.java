package com.pepperjacks.foodplatform.service;

import com.pepperjacks.foodplatform.dto.serviceability.ServiceabilityResponseDto;

public interface ServiceabilityService {
    
    ServiceabilityResponseDto checkServiceability(Double latitude, Double longitude);
}

