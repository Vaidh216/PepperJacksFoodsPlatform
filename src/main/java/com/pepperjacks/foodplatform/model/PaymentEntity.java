package com.pepperjacks.foodplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "provider", nullable = false, length = 50)
    private String provider;
    
    @Column(name = "provider_payment_id")
    private String providerPaymentId;
    
    @Column(name = "provider_order_id")
    private String providerOrderId;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "currency", length = 10)
    private String currency = "INR";
    
    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;
    
    @Column(name = "raw_request", columnDefinition = "TEXT")
    private String rawRequest;
    
    @Column(name = "raw_response", columnDefinition = "TEXT")
    private String rawResponse;
    
    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum PaymentStatus {
        INITIATED, SUCCESS, FAILED, REFUNDED
    }
}

