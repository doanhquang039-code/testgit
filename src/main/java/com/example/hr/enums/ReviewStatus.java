package com.example.hr.enums;

public enum ReviewStatus {
    DRAFT("Nháp"),
    SUBMITTED("Đã gửi"),
    ACKNOWLEDGED("Đã xác nhận"),
    APPROVED("Đã phê duyệt"),
    COMPLETED("Hoàn thành");

    private final String displayName;

    ReviewStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
