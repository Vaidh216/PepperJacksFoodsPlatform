package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistoryEntity, Long> {
    
    List<OrderStatusHistoryEntity> findByOrderIdOrderByCreatedAtAsc(Long orderId);
}

