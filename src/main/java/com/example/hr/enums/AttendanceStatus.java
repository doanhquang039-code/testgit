package com.example.hr.enums;

public enum AttendanceStatus {
    PRESENT,       // Có mặt đúng giờ
    LATE,          // Đi muộn (check-in sau 8:30)
    EARLY_LEAVE,   // Về sớm (check-out trước 17:30)
    ABSENT         // Vắng mặt
}
