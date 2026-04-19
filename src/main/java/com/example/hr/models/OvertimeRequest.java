package com.example.hr.models;

import com.example.hr.enums.OvertimeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity quản lý đơn đăng ký làm thêm giờ (OT).
 */
@Entity
@Table(name = "overtime_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvertimeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "overtime_date", nullable = false)
    private LocalDate overtimeDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "total_hours", precision = 5, scale = 2, nullable = false)
    private BigDecimal totalHours = BigDecimal.ZERO;

    @Column(precision = 3, scale = 1, nullable = false)
    private BigDecimal multiplier = new BigDecimal("1.5");

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OvertimeStatus status = OvertimeStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Business Logic Methods ---

    /**
     * Tự động tính số giờ OT từ start/end time.
     */
    public void calculateTotalHours() {
        if (startTime != null && endTime != null) {
            long minutes = Duration.between(startTime, endTime).toMinutes();
            if (minutes < 0) minutes += 24 * 60; // qua ngày
            this.totalHours = BigDecimal.valueOf(minutes)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Xác định hệ số OT dựa trên ngày (ngày thường / cuối tuần / lễ).
     */
    public void determineMultiplier(boolean isHoliday) {
        if (isHoliday) {
            this.multiplier = new BigDecimal("3.0");
        } else if (overtimeDate != null) {
            int dayOfWeek = overtimeDate.getDayOfWeek().getValue();
            if (dayOfWeek == 6 || dayOfWeek == 7) {
                this.multiplier = new BigDecimal("2.0");
            } else {
                this.multiplier = new BigDecimal("1.5");
            }
        }
    }

    /**
     * Tính tiền OT dựa trên lương cơ bản mỗi giờ.
     */
    public BigDecimal calculateOvertimePay(BigDecimal hourlyRate) {
        if (hourlyRate == null || totalHours == null || multiplier == null) {
            return BigDecimal.ZERO;
        }
        return hourlyRate.multiply(totalHours).multiply(multiplier)
                .setScale(0, RoundingMode.HALF_UP);
    }

    /**
     * Kiểm tra OT có hợp lệ không (tối đa 4 giờ/ngày).
     */
    public boolean isValidDuration() {
        return totalHours != null
                && totalHours.compareTo(BigDecimal.ZERO) > 0
                && totalHours.compareTo(new BigDecimal("4")) <= 0;
    }

    /**
     * Duyệt đơn OT.
     */
    public void approve(User approver) {
        this.status = OvertimeStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Từ chối đơn OT.
     */
    public void reject(User approver, String reason) {
        this.status = OvertimeStatus.REJECTED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
        this.rejectionReason = reason;
        this.updatedAt = LocalDateTime.now();
    }
}
