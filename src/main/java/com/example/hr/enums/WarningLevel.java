package com.example.hr.enums;

/**
 * Mức độ cảnh cáo nhân viên.
 */
public enum WarningLevel {
    VERBAL("Nhắc nhở miệng"),
    WRITTEN("Cảnh cáo bằng văn bản"),
    FINAL("Cảnh cáo lần cuối"),
    TERMINATION("Sa thải");

    private final String displayName;

    WarningLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case VERBAL -> "info";
            case WRITTEN -> "warning";
            case FINAL -> "danger";
            case TERMINATION -> "dark";
        };
    }

    /**
     * Lấy mức cảnh cáo tiếp theo (escalation).
     */
    public WarningLevel next() {
        return switch (this) {
            case VERBAL -> WRITTEN;
            case WRITTEN -> FINAL;
            case FINAL -> TERMINATION;
            case TERMINATION -> TERMINATION;
        };
    }
}
