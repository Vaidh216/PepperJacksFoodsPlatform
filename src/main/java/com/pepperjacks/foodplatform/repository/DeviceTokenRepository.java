package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.DeviceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceTokenEntity, Long> {
    
    Optional<DeviceTokenEntity> findByUserIdAndDeviceId(Long userId, String deviceId);
    
    List<DeviceTokenEntity> findByUserIdAndIsActiveTrue(Long userId);
}

