package com.example.hr.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity gán tài sản công ty cho nhân viên.
 */
@Entity
@Table(name = "asset_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private CompanyAsset asset;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "expected_return")
    private LocalDate expectedReturn;

    @Column(name = "actual_return")
    private LocalDate actualReturn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @Column(name = "condition_on_assign", length = 50)
    private String conditionOnAssign = "GOOD";

    @Column(name = "condition_on_return", length = 50)
    private String conditionOnReturn;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Business Logic ---

    /**
     * Kiểm tra tài sản đã được trả lại chưa.
     */
    public boolean isReturned() {
        return actualReturn != null;
    }

    /**
     * Kiểm tra tài sản quá hạn trả.
     */
    public boolean isOverdue() {
        if (isReturned()) return false;
        return expectedReturn != null && expectedReturn.isBefore(LocalDate.now());
    }

    /**
     * Số ngày quá hạn.
     */
    public long getOverdueDays() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(expectedReturn, LocalDate.now());
    }

    /**
     * Số ngày đã sử dụng.
     */
    public long getDaysUsed() {
        LocalDate endDate = actualReturn != null ? actualReturn : LocalDate.now();
        return ChronoUnit.DAYS.between(assignedDate, endDate);
    }

    /**
     * Trả tài sản.
     */
    public void returnAsset(String condition) {
        this.actualReturn = LocalDate.now();
        this.conditionOnReturn = condition;
        if (asset != null) {
            asset.markAvailable();
        }
    }

    /**
     * Kiểm tra tình trạng tài sản có bị hư hại khi trả.
     */
    public boolean isDamaged() {
        return conditionOnReturn != null
                && (conditionOnReturn.equalsIgnoreCase("DAMAGED")
                    || conditionOnReturn.equalsIgnoreCase("BROKEN"));
    }

    /**
     * Lấy status badge color.
     */
    public String getStatusColor() {
        if (isReturned()) return "secondary";
        if (isOverdue()) return "danger";
        return "success";
    }

    /**
     * Lấy status label.
     */
    public String getStatusLabel() {
        if (isReturned()) return "Đã trả";
        if (isOverdue()) return "Quá hạn (" + getOverdueDays() + " ngày)";
        return "Đang sử dụng";
    }
}
