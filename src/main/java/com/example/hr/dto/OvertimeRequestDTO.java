package com.example.hr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequestDTO {

    private Integer id;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;

    @NotNull(message = "Ngày OT không được để trống")
    private LocalDate overtimeDate;

    @NotNull(message = "Giờ bắt đầu không được để trống")
    private LocalTime startTime;

    @NotNull(message = "Giờ kết thúc không được để trống")
    private LocalTime endTime;

    private BigDecimal totalHours;
    private BigDecimal multiplier;
    private String reason;

    // Dùng cho approval/rejection
    private Integer approvedById;
    private String rejectionReason;
}
