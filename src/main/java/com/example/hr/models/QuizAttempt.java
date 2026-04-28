package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    private Integer score;
    private Integer totalPoints;
    
    @Column(nullable = false)
    private Boolean passed = false;
    
    @Column(columnDefinition = "TEXT")
    private String answers; // JSON của câu trả lời
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
    }
}
