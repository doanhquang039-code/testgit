package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "key_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id", nullable = false)
    private Objective objective;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String measurementType; // PERCENTAGE, NUMBER, BOOLEAN

    @Column(nullable = false)
    private Double targetValue;

    @Column(nullable = false)
    private Double currentValue = 0.0;

    @Column(nullable = false)
    private Double progress = 0.0;

    @Column(nullable = false)
    private String unit = "";

    @Column(nullable = false)
    private Integer weight = 100; // Weight in percentage

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
        calculateProgress();
    }

    public void calculateProgress() {
        if (targetValue == null || targetValue == 0) {
            this.progress = 0.0;
            return;
        }
        this.progress = Math.min(100.0, (currentValue / targetValue) * 100.0);
        this.progress = Math.round(this.progress * 100.0) / 100.0;
    }
}
