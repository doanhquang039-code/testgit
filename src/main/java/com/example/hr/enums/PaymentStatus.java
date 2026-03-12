package com.example.hr.enums;

public enum PaymentStatus {
    PENDING("Chờ thanh toán"),
    PAID("Đã thanh toán"),
    CANCELLED("Đã hủy");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}