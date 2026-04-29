package com.example.hr.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event cho Training (Đào tạo)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEvent {
    private Integer trainingId;
    private String trainingName;
    private Integer employeeId;
    private String employeeName;
    private String eventType; // ENROLLED, STARTED, COMPLETED, CANCELLED, CERTIFICATE_ISSUED
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Double completionPercentage;
    private LocalDateTime timestamp;
}
