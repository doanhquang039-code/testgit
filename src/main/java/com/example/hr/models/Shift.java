package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "shifts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name; // Morning, Afternoon, Night
    
    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column(nullable = false)
    private LocalTime endTime;
    
    private Integer gracePeriodMinutes; // Thời gian cho phép đến muộn
    
    private Integer overtimeThresholdMinutes; // Ngưỡng tính overtime
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    private String description;
}
