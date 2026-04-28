package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false)
    private String status = "ENROLLED"; // ENROLLED, IN_PROGRESS, COMPLETED, FAILED
    
    private Integer progressPercent = 0;
    
    private Integer score;
    
    private LocalDateTime enrolledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    private String certificateUrl;
    
    @PrePersist
    protected void onCreate() {
        enrolledAt = LocalDateTime.now();
    }
}
