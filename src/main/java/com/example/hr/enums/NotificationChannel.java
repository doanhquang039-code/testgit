package com.example.hr.enums;

public enum NotificationChannel {
    IN_APP("In-App"),
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push Notification");

    private final String displayName;

    NotificationChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
