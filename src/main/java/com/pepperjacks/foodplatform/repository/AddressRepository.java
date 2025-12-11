package com.pepperjacks.foodplatform.repository;

import com.pepperjacks.foodplatform.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    
    List<AddressEntity> findByUserId(Long userId);
    
    Optional<AddressEntity> findByUserIdAndIsDefaultTrue(Long userId);
}

