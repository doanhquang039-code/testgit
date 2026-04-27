package com.example.hr.enums;

public enum LeaveType {
    ANNUAL("Annual Leave"),
    SICK("Sick Leave"),
    UNPAID("Unpaid Leave"),
    MATERNITY("Maternity Leave"),
    PATERNITY("Paternity Leave"),
    COMPASSIONATE("Compassionate Leave"),
    STUDY("Study Leave"),
    COMPENSATORY("Compensatory Leave");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
