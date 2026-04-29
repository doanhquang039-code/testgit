package com.example.hr.kafka.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event cho Attendance (Chấm công)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceEvent {
    private Integer attendanceId;
    private Integer userId;
    private String username;
    private String fullName;
    private String eventType; // CHECK_IN, CHECK_OUT, LATE, EARLY_LEAVE, ABSENT
    private LocalDateTime timestamp;
    private String location;
    private Double latitude;
    private Double longitude;
    private String deviceInfo;
    private String notes;
}
