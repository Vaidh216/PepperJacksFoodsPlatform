package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
    List<NotificationEntity> findByUserIdOrderBySentAtDesc(Long userId);
    
    List<NotificationEntity> findByUserIdAndIsReadFalseOrderBySentAtDesc(Long userId);
}

