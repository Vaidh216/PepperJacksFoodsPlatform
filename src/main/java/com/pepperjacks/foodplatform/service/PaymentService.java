package com.pepperjacks.foodplatform.service;

import com.pepperjacks.foodplatform.dto.payment.PaymentRequestDto;
import com.pepperjacks.foodplatform.dto.payment.PaymentResponseDto;

public interface PaymentService {
    
    PaymentResponseDto initiatePayment(Long userId, PaymentRequestDto request);
    
    void handlePaymentWebhook(String payload, String signature);
}

