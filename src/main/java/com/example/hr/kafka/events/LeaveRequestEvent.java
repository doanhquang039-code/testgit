package com.example.hr.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event cho Leave Request (Đơn xin nghỉ phép)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestEvent {
    private Integer leaveRequestId;
    private Integer userId;
    private String username;
    private String fullName;
    private String eventType; // SUBMITTED, APPROVED, REJECTED, CANCELLED
    private String leaveType; // ANNUAL, SICK, UNPAID, etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;
    private String reason;
    private String status;
    private Integer approvedBy;
    private String approverName;
    private LocalDateTime timestamp;
}
