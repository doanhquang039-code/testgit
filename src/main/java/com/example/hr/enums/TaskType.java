package com.example.hr.enums;

public enum TaskType {
    DAILY("Hàng ngày"),
    WEEKLY("Hàng tuần"),
    MONTHLY("Hàng tháng"),
    CREATIVE("Creative"),
    DEVELOPMENT("Development"),
    DESIGN("Design"),
    TESTING("Testing"),
    BUGFIX("Bug Fix"),
    FEATURE("Feature"),
    MAINTENANCE("Maintenance"),
    RESEARCH("Research"),
    DOCUMENTATION("Documentation"),
    MEETING("Meeting"),
    REVIEW("Review"),
    DEPLOYMENT("Deployment"),
    OTHER("Other");

    private final String displayName;

    TaskType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}