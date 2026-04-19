package com.example.hr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO phân tích nhân viên nâng cao.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeAnalyticsDTO {

    private long totalHeadcount;
    private double avgTenureMonths;
    private double turnoverRatePercent;
    private long newHiresThisYear;
    private long terminationsThisYear;

    // Phân bố theo phòng ban
    private Map<String, Long> headcountByDepartment;

    // Phân bố theo vị trí
    private Map<String, Long> headcountByPosition;

    // Phân bố theo trạng thái
    private Map<String, Long> headcountByStatus;

    // Xu hướng headcount theo tháng (12 tháng)
    private List<MonthlyHeadcount> headcountTrending;

    // Chi phí nhân sự trung bình
    private BigDecimal avgSalary;
    private BigDecimal totalSalaryCost;

    // Training metrics
    private double trainingCompletionRate;
    private long totalTrainingHours;

    // Warning metrics
    private long totalActiveWarnings;
    private double warningRatePercent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyHeadcount {
        private int year;
        private int month;
        private long headcount;
        private long newHires;
        private long terminations;
    }
}
