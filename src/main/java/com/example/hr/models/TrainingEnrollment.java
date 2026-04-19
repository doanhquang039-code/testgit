package com.example.hr.models;

import com.example.hr.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity ghi danh nhân viên vào chương trình đào tạo.
 */
@Entity
@Table(name = "training_enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "program_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private TrainingProgram program;

    @Column(name = "enrolled_at", nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "certificate_url", length = 500)
    private String certificateUrl;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // --- Business Logic ---

    /**
     * Kiểm tra đạt/không đạt (threshold: 60 điểm).
     */
    public boolean isPassed() {
        return score != null && score.compareTo(new BigDecimal("60")) >= 0;
    }

    /**
     * Lấy grade label dựa trên điểm số.
     */
    public String getGradeLabel() {
        if (score == null) return "Chưa có điểm";
        double s = score.doubleValue();
        if (s >= 90) return "Xuất sắc (A)";
        if (s >= 80) return "Giỏi (B+)";
        if (s >= 70) return "Khá (B)";
        if (s >= 60) return "Trung bình (C)";
        return "Không đạt (F)";
    }

    /**
     * Lấy color badge cho grade.
     */
    public String getGradeColor() {
        if (score == null) return "secondary";
        double s = score.doubleValue();
        if (s >= 90) return "success";
        if (s >= 80) return "primary";
        if (s >= 70) return "info";
        if (s >= 60) return "warning";
        return "danger";
    }

    /**
     * Hoàn thành khóa học.
     */
    public void complete(BigDecimal finalScore) {
        this.score = finalScore;
        this.completedAt = LocalDateTime.now();
        this.status = isPassed() ? EnrollmentStatus.COMPLETED : EnrollmentStatus.FAILED;
    }

    /**
     * Bỏ học.
     */
    public void drop() {
        this.status = EnrollmentStatus.DROPPED;
    }

    /**
     * Kiểm tra có certificate hay không.
     */
    public boolean hasCertificate() {
        return certificateUrl != null && !certificateUrl.isBlank();
    }
}
