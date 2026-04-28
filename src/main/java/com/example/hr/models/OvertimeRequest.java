package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "overtime_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDate overtimeDate;
    
    @Column(nullable = false)
    private Double hours; // Số giờ làm thêm
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    private LocalDateTime approvedAt;
    
    private String rejectionReason;
    
    private Double multiplier = 1.5; // Hệ số lương OT
    
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
