package com.pepperjacks.foodplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kitchens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KitchenEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    
    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
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
}

