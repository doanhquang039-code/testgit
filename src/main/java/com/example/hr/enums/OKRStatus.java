package com.example.hr.enums;

public enum OKRStatus {
    DRAFT("Draft"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    OKRStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
