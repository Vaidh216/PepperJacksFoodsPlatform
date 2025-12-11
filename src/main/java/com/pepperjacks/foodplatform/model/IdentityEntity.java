package com.pepperjacks.foodplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "identities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"provider", "provider_user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "provider", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private IdentityProvider provider;
    
    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum IdentityProvider {
        PHONE, EMAIL, GOOGLE
    }
}

