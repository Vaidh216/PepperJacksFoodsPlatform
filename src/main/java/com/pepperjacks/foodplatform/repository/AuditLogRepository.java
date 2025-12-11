package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    
    List<AuditLogEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    List<AuditLogEntity> findByEntityTypeAndEntityId(String entityType, Long entityId);
}

