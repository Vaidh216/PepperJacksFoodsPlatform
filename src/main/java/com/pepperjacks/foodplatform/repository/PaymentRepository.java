package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.PaymentEntity;
import com.pepperjacks.foodplatform.model.PaymentEntity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    
    Optional<PaymentEntity> findByOrderId(Long orderId);
    
    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
    
    Optional<PaymentEntity> findByProviderPaymentId(String providerPaymentId);
    
    List<PaymentEntity> findByUserId(Long userId);
    
    List<PaymentEntity> findByStatus(PaymentStatus status);
}

