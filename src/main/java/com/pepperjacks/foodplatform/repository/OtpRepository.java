package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    
    Optional<OtpEntity> findTopByPhoneAndIsVerifiedFalseOrderByCreatedAtDesc(String phone);
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}

