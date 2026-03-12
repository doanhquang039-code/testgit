package com.example.hr.enums;

public enum TaskType {
    DAILY("Hàng ngày"),
    WEEKLY("Hàng tuần"),
    MONTHLY("Hàng tháng"),
    CREATIVE("Creative");


    private final String displayName;

    TaskType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}