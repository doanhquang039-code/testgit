package com.example.hr.models;

import com.example.hr.enums.BenefitStatus;
import com.example.hr.enums.BenefitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity phúc lợi nhân viên (bảo hiểm, hỗ trợ, v.v.).
 */
@Entity
@Table(name = "employee_benefit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type", nullable = false, length = 30)
    private BenefitType benefitType;

    @Column(name = "benefit_name", nullable = false, length = 200)
    private String benefitName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "monetary_value", precision = 15, scale = 2)
    private BigDecimal monetaryValue = BigDecimal.ZERO;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BenefitStatus status = BenefitStatus.ACTIVE;

    @Column(length = 200)
    private String provider;

    @Column(name = "policy_number", length = 100)
    private String policyNumber;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Business Logic ---

    /**
     * Kiểm tra benefit đã hết hạn chưa.
     */
    public boolean isExpired() {
        return endDate != null && endDate.isBefore(LocalDate.now());
    }

    /**
     * Kiểm tra benefit sắp hết hạn (trong 30 ngày).
     */
    public boolean isExpiringSoon() {
        return isExpiringSoon(30);
    }

    /**
     * Kiểm tra benefit sắp hết hạn trong N ngày.
     */
    public boolean isExpiringSoon(int days) {
        if (endDate == null) return false;
        LocalDate today = LocalDate.now();
        return endDate.isAfter(today) && endDate.isBefore(today.plusDays(days + 1));
    }

    /**
     * Tính số tháng còn lại.
     */
    public long getRemainingMonths() {
        if (endDate == null) return -1; // vô thời hạn
        LocalDate today = LocalDate.now();
        if (endDate.isBefore(today)) return 0;
        return ChronoUnit.MONTHS.between(today, endDate);
    }

    /**
     * Tính tổng chi phí dựa trên thời gian sử dụng.
     */
    public BigDecimal getTotalCost() {
        if (monetaryValue == null || startDate == null) return BigDecimal.ZERO;
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        long months = ChronoUnit.MONTHS.between(startDate, end);
        if (months <= 0) months = 1;
        return monetaryValue.multiply(BigDecimal.valueOf(months));
    }

    /**
     * Auto-expire nếu đã quá endDate.
     */
    public boolean autoExpireIfNeeded() {
        if (isExpired() && status == BenefitStatus.ACTIVE) {
            this.status = BenefitStatus.EXPIRED;
            return true;
        }
        return false;
    }

    /**
     * Lấy icon theo loại phúc lợi.
     */
    public String getBenefitIcon() {
        return switch (benefitType) {
            case HEALTH_INSURANCE -> "bi-hospital";
            case LIFE_INSURANCE -> "bi-shield-check";
            case MATERNITY -> "bi-balloon-heart";
            case HOUSING -> "bi-house";
            case TRANSPORTATION -> "bi-bus-front";
            case MEAL -> "bi-cup-hot";
            case EDUCATION -> "bi-book";
            case PHONE -> "bi-phone";
            case OTHER -> "bi-gift";
        };
    }
}
