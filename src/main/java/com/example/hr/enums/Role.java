package com.example.hr.enums;

public enum Role {
    ADMIN("Quản trị viên"),
    MANAGER("Quản lý"),
    HIRING("Nhân sự/Tuyển dụng"),
    USER("Nhân viên");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}