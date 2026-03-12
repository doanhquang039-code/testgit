package com.example.hr.enums;

public enum LeaveStatus {
    PENDING("Chờ duyệt"),
    APPROVED("Đã phê duyệt"),
    REJECTED("Từ chối");

    private final String displayName;

    LeaveStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}