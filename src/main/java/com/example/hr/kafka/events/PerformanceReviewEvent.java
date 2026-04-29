package com.example.hr.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event cho Performance Review (Đánh giá hiệu suất)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceReviewEvent {
    private Integer reviewId;
    private Integer employeeId;
    private String employeeName;
    private Integer reviewerId;
    private String reviewerName;
    private String eventType; // CREATED, SUBMITTED, APPROVED, COMPLETED
    private String reviewPeriod;
    private LocalDate reviewDate;
    private Double overallRating;
    private String status;
    private LocalDateTime timestamp;
}
