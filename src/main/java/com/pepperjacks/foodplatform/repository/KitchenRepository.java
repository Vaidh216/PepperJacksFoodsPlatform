package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.KitchenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitchenRepository extends JpaRepository<KitchenEntity, Long> {
    
    List<KitchenEntity> findByIsActiveTrue();
}

