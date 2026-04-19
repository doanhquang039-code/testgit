package com.example.hr.enums;

/**
 * Trạng thái tài sản công ty.
 */
public enum AssetStatus {
    AVAILABLE("Sẵn sàng"),
    ASSIGNED("Đã giao"),
    MAINTENANCE("Bảo trì"),
    RETIRED("Thanh lý"),
    LOST("Thất lạc");

    private final String displayName;

    AssetStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return switch (this) {
            case AVAILABLE -> "success";
            case ASSIGNED -> "primary";
            case MAINTENANCE -> "warning";
            case RETIRED -> "secondary";
            case LOST -> "danger";
        };
    }
}
