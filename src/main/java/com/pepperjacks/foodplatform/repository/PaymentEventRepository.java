package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.PaymentEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEventEntity, Long> {
    
    List<PaymentEventEntity> findByPaymentIdOrderByCreatedAtDesc(Long paymentId);
}

