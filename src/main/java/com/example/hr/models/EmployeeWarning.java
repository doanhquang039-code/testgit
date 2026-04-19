package com.example.hr.models;

import com.example.hr.enums.WarningLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity cảnh cáo / kỷ luật nhân viên.
 */
@Entity
@Table(name = "employee_warning")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeWarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issued_by", nullable = false)
    private User issuedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "warning_level", nullable = false, length = 20)
    private WarningLevel warningLevel = WarningLevel.VERBAL;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_acknowledged")
    private Boolean isAcknowledged = false;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Business Logic ---

    /**
     * Kiểm tra cảnh cáo còn hiệu lực hay không.
     */
    public boolean isActive() {
        if (expiryDate == null) return true;
        return !expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Nhân viên xác nhận đã đọc cảnh cáo.
     */
    public void acknowledge() {
        this.isAcknowledged = true;
        this.acknowledgedAt = LocalDateTime.now();
    }

    /**
     * Kiểm tra có cần escalation không (cảnh cáo đã quá 30 ngày mà chưa acknowledge).
     */
    public boolean needsEscalation() {
        if (Boolean.TRUE.equals(isAcknowledged)) return false;
        return issuedDate != null
                && issuedDate.isBefore(LocalDate.now().minusDays(30));
    }

    /**
     * Lấy mức cảnh cáo tiếp theo nếu cần escalation.
     */
    public WarningLevel getNextEscalationLevel() {
        return warningLevel.next();
    }

    /**
     * Kiểm tra có phải level nghiêm trọng không (FINAL hoặc TERMINATION).
     */
    public boolean isSevere() {
        return warningLevel == WarningLevel.FINAL
                || warningLevel == WarningLevel.TERMINATION;
    }

    /**
     * Tính số ngày kể từ khi ban hành.
     */
    public long getDaysSinceIssued() {
        if (issuedDate == null) return 0;
        return java.time.temporal.ChronoUnit.DAYS.between(issuedDate, LocalDate.now());
    }
}
