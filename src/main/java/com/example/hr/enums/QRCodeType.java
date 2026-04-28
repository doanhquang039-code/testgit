package com.example.hr.enums;

public enum QRCodeType {
    CHECK_IN("Check-in/Check-out"),
    ASSET("Asset Tracking"),
    DOCUMENT("Document Verification"),
    EVENT("Event Registration"),
    MEETING_ROOM("Meeting Room"),
    PARKING("Parking Spot");

    private final String displayName;

    QRCodeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
