package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "benefit_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String category; // HEALTH_INSURANCE, LIFE_INSURANCE, GYM, MEAL, TRANSPORT
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private BigDecimal employeeCost; // Chi phí nhân viên trả
    private BigDecimal companyCost; // Chi phí công ty trả
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    private String provider; // Nhà cung cấp
    
    @Column(columnDefinition = "TEXT")
    private String terms; // Điều khoản
    
    private LocalDateTime createdAt;
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
