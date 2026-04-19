package com.example.hr.enums;

/**
 * Trạng thái phúc lợi.
 */
public enum BenefitStatus {
    ACTIVE("Đang hiệu lực"),
    EXPIRED("Hết hạn"),
    PENDING("Chờ kích hoạt"),
    CANCELLED("Đã hủy");

    private final String displayName;

    BenefitStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case ACTIVE -> "success";
            case EXPIRED -> "secondary";
            case PENDING -> "warning";
            case CANCELLED -> "danger";
        };
    }
}
