package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);
    
    List<RefreshTokenEntity> findByUserId(Long userId);
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}

