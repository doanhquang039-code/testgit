package com.example.hr.enums;

/**
 * Trạng thái chương trình đào tạo.
 */
public enum TrainingStatus {
    PLANNED("Kế hoạch"),
    IN_PROGRESS("Đang diễn ra"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");

    private final String displayName;

    TrainingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case PLANNED -> "info";
            case IN_PROGRESS -> "primary";
            case COMPLETED -> "success";
            case CANCELLED -> "secondary";
        };
    }
}
