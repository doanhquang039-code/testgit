package com.example.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO báo cáo tổng hợp tháng/năm.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReportDTO {

    private int month;
    private int year;

    // Nhân sự
    private long totalHeadcount;
    private long newHires;
    private long terminations;
    private double attendanceRate;

    // Tài chính
    private BigDecimal totalPayroll;
    private BigDecimal totalOvertime;
    private BigDecimal totalBenefitCost;
    private BigDecimal totalTrainingBudget;

    // Hoạt động
    private long leaveRequestsCount;
    private long overtimeRequestsCount;
    private long warningsIssuedCount;
    private long trainingsCompletedCount;

    // Phân bố theo phòng ban
    private Map<String, BigDecimal> payrollByDepartment;
    private Map<String, Long> headcountByDepartment;
    private Map<String, Double> attendanceByDepartment;
}
