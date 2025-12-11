package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.serviceability.ServiceabilityCheckDto;
import com.pepperjacks.foodplatform.dto.serviceability.ServiceabilityResponseDto;
import com.pepperjacks.foodplatform.service.ServiceabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/serviceability")
@RequiredArgsConstructor
public class ServiceabilityController {
    
    private final ServiceabilityService serviceabilityService;
    
    @PostMapping("/check")
    public ResponseEntity<ServiceabilityResponseDto> checkServiceability(
            @Valid @RequestBody ServiceabilityCheckDto request) {
        ServiceabilityResponseDto response = serviceabilityService.checkServiceability(
                request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(response);
    }
}

