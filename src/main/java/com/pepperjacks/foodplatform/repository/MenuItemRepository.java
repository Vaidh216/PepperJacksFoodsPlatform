package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    
    List<MenuItemEntity> findByKitchenIdAndIsAvailableTrue(Long kitchenId);
    
    List<MenuItemEntity> findByKitchenId(Long kitchenId);
    
    List<MenuItemEntity> findByCategory(String category);
}

