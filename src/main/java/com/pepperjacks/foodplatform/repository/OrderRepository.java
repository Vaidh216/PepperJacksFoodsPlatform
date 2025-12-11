package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.OrderEntity;
import com.pepperjacks.foodplatform.model.OrderEntity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
    List<OrderEntity> findByUserId(Long userId);
    
    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<OrderEntity> findByKitchenIdAndStatus(Long kitchenId, OrderStatus status);
    
    List<OrderEntity> findByStatus(OrderStatus status);
    
    List<OrderEntity> findByKitchenIdOrderByCreatedAtDesc(Long kitchenId);
}

