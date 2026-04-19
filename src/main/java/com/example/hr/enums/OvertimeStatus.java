package com.example.hr.enums;

/**
 * Trạng thái đơn làm thêm giờ (OT).
 */
public enum OvertimeStatus {
    PENDING("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Từ chối"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OvertimeStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case PENDING -> "warning";
            case APPROVED -> "success";
            case REJECTED -> "danger";
            case CANCELLED -> "secondary";
        };
    }
}
