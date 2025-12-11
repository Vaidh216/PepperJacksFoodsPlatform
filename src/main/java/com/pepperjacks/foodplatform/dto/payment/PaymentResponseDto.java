package com.pepperjacks.foodplatform.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {
    
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String status;
    private String provider;
    private String providerPaymentId;
    private String paymentUrl;
}

