package com.example.hr.enums;

/**
 * Loại phúc lợi nhân viên.
 */
public enum BenefitType {
    HEALTH_INSURANCE("Bảo hiểm y tế"),
    LIFE_INSURANCE("Bảo hiểm nhân thọ"),
    MATERNITY("Thai sản"),
    HOUSING("Hỗ trợ nhà ở"),
    TRANSPORTATION("Hỗ trợ đi lại"),
    MEAL("Hỗ trợ ăn trưa"),
    EDUCATION("Hỗ trợ học tập"),
    PHONE("Hỗ trợ điện thoại"),
    OTHER("Khác");

    private final String displayName;

    BenefitType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
