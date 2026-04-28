package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_referrals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReferral {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", nullable = false)
    private User referrer; // Người giới thiệu
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;
    
    @Column(nullable = false)
    private String candidateName;
    
    @Column(nullable = false)
    private String candidateEmail;
    
    @Column(nullable = false)
    private String candidatePhone;
    
    private String resumeUrl;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(nullable = false)
    private String status = "SUBMITTED"; // SUBMITTED, SCREENING, INTERVIEWING, HIRED, REJECTED
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate; // Link to actual candidate if created
    
    private BigDecimal bonusAmount; // Tiền thưởng nếu tuyển được
    
    @Column(nullable = false)
    private Boolean bonusPaid = false;
    
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
