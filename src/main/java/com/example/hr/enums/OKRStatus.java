package com.example.hr.enums;

public enum OKRStatus {
    DRAFT("Nháp"),
    ACTIVE("Đang hoạt động"),
    NOT_STARTED("Chưa bắt đầu"),
    IN_PROGRESS("Đang thực hiện"),
    AT_RISK("Có rủi ro"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");

    private final String displayName;

    OKRStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
