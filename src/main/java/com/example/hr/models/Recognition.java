package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recognitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recognition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient; // Người nhận
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_id", nullable = false)
    private User giver; // Người tặng
    
    @Column(nullable = false)
    private String type; // THANK_YOU, GREAT_JOB, TEAM_PLAYER, INNOVATION, LEADERSHIP
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;
    
    private Integer points = 0; // Điểm thưởng
    
    @Column(nullable = false)
    private Boolean isPublic = true;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
