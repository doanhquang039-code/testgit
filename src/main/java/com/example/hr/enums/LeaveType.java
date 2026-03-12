package com.example.hr.enums;

public enum LeaveType {
    ANNUAL("Nghỉ phép năm"),
    SICK("Nghỉ ốm"),
    UNPAID("Nghỉ không lương");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}