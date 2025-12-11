package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.IdentityEntity;
import com.pepperjacks.foodplatform.model.IdentityEntity.IdentityProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityEntity, Long> {
    
    Optional<IdentityEntity> findByProviderAndProviderUserId(IdentityProvider provider, String providerUserId);
    
    List<IdentityEntity> findByUserId(Long userId);
    
    boolean existsByProviderAndProviderUserId(IdentityProvider provider, String providerUserId);
}

