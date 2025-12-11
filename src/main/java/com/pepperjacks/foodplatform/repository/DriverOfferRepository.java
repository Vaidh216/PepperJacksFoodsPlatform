package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.DriverOfferEntity;
import com.pepperjacks.foodplatform.model.DriverOfferEntity.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverOfferRepository extends JpaRepository<DriverOfferEntity, Long> {
    
    List<DriverOfferEntity> findByOrderId(Long orderId);
    
    List<DriverOfferEntity> findByDriverIdAndStatus(Long driverId, OfferStatus status);
    
    Optional<DriverOfferEntity> findByOrderIdAndDriverId(Long orderId, Long driverId);
}

