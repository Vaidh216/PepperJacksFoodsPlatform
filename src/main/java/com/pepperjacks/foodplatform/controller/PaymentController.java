package com.pepperjacks.foodplatform.controller;

import com.pepperjacks.foodplatform.dto.payment.PaymentRequestDto;
import com.pepperjacks.foodplatform.dto.payment.PaymentResponseDto;
import com.pepperjacks.foodplatform.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDto> initiatePayment(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody PaymentRequestDto request) {
        PaymentResponseDto response = paymentService.initiatePayment(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Signature", required = false) String signature) {
        paymentService.handlePaymentWebhook(payload, signature);
        return ResponseEntity.ok("Webhook processed");
    }
}

