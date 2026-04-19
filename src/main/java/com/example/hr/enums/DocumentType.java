package com.example.hr.enums;

/**
 * Loại tài liệu nhân viên.
 */
public enum DocumentType {
    IDENTITY_CARD("CMND/CCCD"),
    DEGREE("Bằng cấp"),
    CERTIFICATE("Chứng chỉ"),
    CONTRACT_SCAN("Scan hợp đồng"),
    RESUME("Hồ sơ xin việc"),
    HEALTH_CHECK("Khám sức khỏe"),
    OTHER("Khác");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
