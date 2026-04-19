package com.example.hr.models;

import com.example.hr.enums.AssetStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity tài sản công ty (laptop, monitor, bàn, điện thoại, v.v.).
 */
@Entity
@Table(name = "company_asset")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "asset_name", nullable = false, length = 200)
    private String assetName;

    @Column(name = "asset_code", unique = true, length = 50)
    private String assetCode;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice = BigDecimal.ZERO;

    @Column(name = "current_value", precision = 15, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssetStatus status = AssetStatus.AVAILABLE;

    @Column(length = 200)
    private String location;

    @Column(name = "warranty_expiry")
    private LocalDate warrantyExpiry;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // --- Business Logic ---

    /**
     * Tính giá trị khấu hao (straight-line depreciation, 3 năm).
     */
    public BigDecimal calculateDepreciation(int usefulLifeYears) {
        if (purchasePrice == null || purchaseDate == null || usefulLifeYears <= 0) {
            return BigDecimal.ZERO;
        }
        long monthsUsed = ChronoUnit.MONTHS.between(purchaseDate, LocalDate.now());
        long totalMonths = (long) usefulLifeYears * 12;
        if (monthsUsed >= totalMonths) {
            return purchasePrice; // fully depreciated
        }
        return purchasePrice.multiply(BigDecimal.valueOf(monthsUsed))
                .divide(BigDecimal.valueOf(totalMonths), 0, RoundingMode.HALF_UP);
    }

    /**
     * Cập nhật giá trị hiện tại (current value = purchase - depreciation).
     */
    public void updateCurrentValue(int usefulLifeYears) {
        BigDecimal dep = calculateDepreciation(usefulLifeYears);
        this.currentValue = purchasePrice.subtract(dep).max(BigDecimal.ZERO);
    }

    /**
     * Kiểm tra bảo hành còn hiệu lực.
     */
    public boolean isUnderWarranty() {
        return warrantyExpiry != null && !warrantyExpiry.isBefore(LocalDate.now());
    }

    /**
     * Kiểm tra bảo hành sắp hết (trong 30 ngày).
     */
    public boolean isWarrantyExpiringSoon() {
        if (warrantyExpiry == null) return false;
        LocalDate today = LocalDate.now();
        return warrantyExpiry.isAfter(today)
                && warrantyExpiry.isBefore(today.plusDays(31));
    }

    /**
     * Kiểm tra tài sản có sẵn sàng để giao.
     */
    public boolean isAvailableForAssignment() {
        return status == AssetStatus.AVAILABLE;
    }

    /**
     * Đánh dấu đã giao.
     */
    public void markAssigned() {
        this.status = AssetStatus.ASSIGNED;
    }

    /**
     * Đánh dấu sẵn sàng (khi trả lại).
     */
    public void markAvailable() {
        this.status = AssetStatus.AVAILABLE;
    }

    /**
     * Tính tuổi tài sản (năm).
     */
    public double getAssetAgeYears() {
        if (purchaseDate == null) return 0;
        long days = ChronoUnit.DAYS.between(purchaseDate, LocalDate.now());
        return days / 365.25;
    }

    /**
     * Lấy icon theo loại tài sản.
     */
    public String getCategoryIcon() {
        if (category == null) return "bi-box";
        return switch (category.toUpperCase()) {
            case "LAPTOP" -> "bi-laptop";
            case "MONITOR" -> "bi-display";
            case "PHONE" -> "bi-phone";
            case "FURNITURE" -> "bi-building";
            case "PRINTER" -> "bi-printer";
            case "NETWORK" -> "bi-router";
            default -> "bi-box";
        };
    }

    /**
     * Tính tỷ lệ khấu hao (%).
     */
    public double getDepreciationPercentage(int usefulLifeYears) {
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        BigDecimal dep = calculateDepreciation(usefulLifeYears);
        return dep.divide(purchasePrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).doubleValue();
    }
}
