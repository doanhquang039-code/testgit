package com.example.hr.models;

import com.example.hr.enums.TrainingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity chương trình đào tạo nội bộ/ngoại bộ.
 */
@Entity
@Table(name = "training_program")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "program_name", nullable = false, length = 200)
    private String programName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "max_capacity")
    private Integer maxCapacity = 30;

    @Column(length = 200)
    private String location;

    @Column(name = "training_type", length = 30)
    private String trainingType = "INTERNAL";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrainingStatus status = TrainingStatus.PLANNED;

    @Column(precision = 15, scale = 2)
    private BigDecimal budget = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainingEnrollment> enrollments = new ArrayList<>();

    // --- Business Logic ---

    /**
     * Số ngày đào tạo.
     */
    public long getDurationDays() {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * Số slot còn trống.
     */
    public int getAvailableSlots() {
        int enrolled = enrollments != null ? enrollments.size() : 0;
        return Math.max(0, (maxCapacity != null ? maxCapacity : 30) - enrolled);
    }

    /**
     * Kiểm tra chương trình đã đầy chưa.
     */
    public boolean isFull() {
        return getAvailableSlots() <= 0;
    }

    /**
     * Kiểm tra chương trình đang diễn ra.
     */
    public boolean isOngoing() {
        LocalDate today = LocalDate.now();
        return startDate != null && endDate != null
                && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    /**
     * Kiểm tra chương trình sắp diễn ra (trong vòng N ngày).
     */
    public boolean isUpcoming(int days) {
        if (startDate == null) return false;
        LocalDate today = LocalDate.now();
        return startDate.isAfter(today) && startDate.isBefore(today.plusDays(days + 1));
    }

    /**
     * Tính chi phí trung bình trên mỗi học viên.
     */
    public BigDecimal getCostPerEnrollee() {
        if (budget == null || enrollments == null || enrollments.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return budget.divide(BigDecimal.valueOf(enrollments.size()), 0,
                java.math.RoundingMode.HALF_UP);
    }

    /**
     * Tỷ lệ hoàn thành.
     */
    public double getCompletionRate() {
        if (enrollments == null || enrollments.isEmpty()) return 0;
        long completed = enrollments.stream()
                .filter(e -> e.getStatus() == com.example.hr.enums.EnrollmentStatus.COMPLETED)
                .count();
        return (double) completed / enrollments.size() * 100;
    }

    /**
     * Lấy progress icon tùy status.
     */
    public String getStatusIcon() {
        return switch (status) {
            case PLANNED -> "bi-calendar-event";
            case IN_PROGRESS -> "bi-play-circle";
            case COMPLETED -> "bi-check-circle";
            case CANCELLED -> "bi-x-circle";
        };
    }
}
