package com.pepperjacks.foodplatform.service.impl;

import com.pepperjacks.foodplatform.dto.payment.PaymentRequestDto;
import com.pepperjacks.foodplatform.dto.payment.PaymentResponseDto;
import com.pepperjacks.foodplatform.model.OrderEntity;
import com.pepperjacks.foodplatform.model.PaymentEntity;
import com.pepperjacks.foodplatform.model.PaymentEntity.PaymentStatus;
import com.pepperjacks.foodplatform.model.UserEntity;
import com.pepperjacks.foodplatform.repository.OrderRepository;
import com.pepperjacks.foodplatform.repository.PaymentRepository;
import com.pepperjacks.foodplatform.repository.UserRepository;
import com.pepperjacks.foodplatform.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public PaymentResponseDto initiatePayment(Long userId, PaymentRequestDto request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }
        
        // Create payment record
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(order.getId());
        payment.setUserId(userId);
        payment.setProvider("PAYU");
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.INITIATED);
        payment.setIdempotencyKey(UUID.randomUUID().toString());
        paymentRepository.save(payment);
        
        // TODO: Integrate with PayU
        String mockPaymentUrl = "https://payu.in/checkout?payment_id=" + payment.getId();
        
        log.info("Payment initiated for order {} with payment ID {}", order.getId(), payment.getId());
        
        return PaymentResponseDto.builder()
                .paymentId(payment.getId())
                .orderId(order.getId())
                .amount(order.getTotalAmount())
                .status(payment.getStatus().name())
                .provider("PAYU")
                .paymentUrl(mockPaymentUrl)
                .build();
    }
    
    @Override
    @Transactional
    public void handlePaymentWebhook(String payload, String signature) {
        // TODO: Verify webhook signature
        // TODO: Parse payload
        // TODO: Update payment status
        // TODO: Update order status
        // TODO: Create payment event
        log.info("Received payment webhook: {}", payload);
        throw new RuntimeException("Payment webhook handling not implemented yet");
    }
}
