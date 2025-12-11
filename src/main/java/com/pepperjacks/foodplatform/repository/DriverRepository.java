package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    
    Optional<DriverEntity> findByUserId(Long userId);
    
    List<DriverEntity> findByIsAvailableTrueAndIsActiveTrue();
}

