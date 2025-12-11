package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    
    Optional<DeliveryEntity> findByOrderId(Long orderId);
    
    List<DeliveryEntity> findByDriverId(Long driverId);
    
    List<DeliveryEntity> findByDriverIdOrderByAssignedAtDesc(Long driverId);
}

