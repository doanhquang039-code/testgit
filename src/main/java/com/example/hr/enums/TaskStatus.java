package com.example.hr.enums;

public enum TaskStatus {
    PENDING("Chưa bắt đầu"),
    IN_PROGRESS("Đang thực hiện"),
    COMPLETED("Hoàn thành"),
    CANCELED("Đã hủy");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}