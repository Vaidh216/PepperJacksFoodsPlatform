package com.pepperjacks.foodplatform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_offers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"order_id", "driver_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverOfferEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "driver_id", nullable = false)
    private Long driverId;
    
    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private OfferStatus status;
    
    @Column(name = "offered_at")
    private LocalDateTime offeredAt;
    
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    
    @PrePersist
    protected void onCreate() {
        offeredAt = LocalDateTime.now();
    }
    
    public enum OfferStatus {
        SENT, ACCEPTED, REJECTED, EXPIRED
    }
}

