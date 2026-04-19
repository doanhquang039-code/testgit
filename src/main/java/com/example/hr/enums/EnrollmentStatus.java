package com.example.hr.enums;

/**
 * Trạng thái ghi danh đào tạo.
 */
public enum EnrollmentStatus {
    ENROLLED("Đã ghi danh"),
    IN_PROGRESS("Đang học"),
    COMPLETED("Hoàn thành"),
    DROPPED("Bỏ học"),
    FAILED("Không đạt");

    private final String displayName;

    EnrollmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case ENROLLED -> "info";
            case IN_PROGRESS -> "primary";
            case COMPLETED -> "success";
            case DROPPED -> "secondary";
            case FAILED -> "danger";
        };
    }
}
