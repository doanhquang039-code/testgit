package com.example.hr.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility xử lý ngày giờ cho HR system.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {}

    private static final DateTimeFormatter VN_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter VN_DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Tính số ngày làm việc giữa 2 ngày (loại bỏ T7, CN).
     */
    public static long countBusinessDays(LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) return 0;

        long days = 0;
        LocalDate current = start;
        while (!current.isAfter(end)) {
            DayOfWeek dow = current.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                days++;
            }
            current = current.plusDays(1);
        }
        return days;
    }

    /**
     * Kiểm tra có phải ngày làm việc (T2-T6).
     */
    public static boolean isBusinessDay(LocalDate date) {
        if (date == null) return false;
        DayOfWeek dow = date.getDayOfWeek();
        return dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY;
    }

    /**
     * Lấy ngày làm việc tiếp theo.
     */
    public static LocalDate nextBusinessDay(LocalDate date) {
        LocalDate next = date.plusDays(1);
        while (!isBusinessDay(next)) {
            next = next.plusDays(1);
        }
        return next;
    }

    /**
     * Lấy ngày đầu tháng.
     */
    public static LocalDate firstDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    /**
     * Lấy ngày cuối tháng.
     */
    public static LocalDate lastDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
    }

    /**
     * Lấy danh sách ngày làm việc trong tháng.
     */
    public static List<LocalDate> getBusinessDaysInMonth(int year, int month) {
        LocalDate start = firstDayOfMonth(year, month);
        LocalDate end = lastDayOfMonth(year, month);

        List<LocalDate> businessDays = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            if (isBusinessDay(current)) {
                businessDays.add(current);
            }
            current = current.plusDays(1);
        }
        return businessDays;
    }

    /**
     * Số ngày làm việc trong tháng.
     */
    public static int countBusinessDaysInMonth(int year, int month) {
        return getBusinessDaysInMonth(year, month).size();
    }

    /**
     * Format ngày theo kiểu VN (dd/MM/yyyy).
     */
    public static String formatDateVN(LocalDate date) {
        if (date == null) return "";
        return date.format(VN_DATE_FORMAT);
    }

    /**
     * Format datetime theo kiểu VN.
     */
    public static String formatDateTimeVN(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(VN_DATETIME_FORMAT);
    }

    /**
     * Tính thâm niên (seniority) tính bằng tháng.
     */
    public static long calculateSeniorityMonths(LocalDate startDate) {
        if (startDate == null) return 0;
        return ChronoUnit.MONTHS.between(startDate, LocalDate.now());
    }

    /**
     * Tính thâm niên tính bằng năm (làm tròn).
     */
    public static double calculateSeniorityYears(LocalDate startDate) {
        if (startDate == null) return 0;
        long months = calculateSeniorityMonths(startDate);
        return months / 12.0;
    }

    /**
     * Format khoảng thời gian "friendly" (VD: "2 năm 3 tháng").
     */
    public static String formatDuration(LocalDate start, LocalDate end) {
        if (start == null) return "N/A";
        LocalDate effectiveEnd = end != null ? end : LocalDate.now();
        long months = ChronoUnit.MONTHS.between(start, effectiveEnd);
        long years = months / 12;
        long remainingMonths = months % 12;

        if (years > 0 && remainingMonths > 0) {
            return years + " năm " + remainingMonths + " tháng";
        } else if (years > 0) {
            return years + " năm";
        } else if (remainingMonths > 0) {
            return remainingMonths + " tháng";
        }
        long days = ChronoUnit.DAYS.between(start, effectiveEnd);
        return days + " ngày";
    }

    /**
     * Relative time ("3 giờ trước", "2 ngày trước").
     */
    public static String timeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        long minutes = ChronoUnit.MINUTES.between(dateTime, LocalDateTime.now());

        if (minutes < 1) return "Vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        long hours = minutes / 60;
        if (hours < 24) return hours + " giờ trước";
        long days = hours / 24;
        if (days < 30) return days + " ngày trước";
        long months = days / 30;
        if (months < 12) return months + " tháng trước";
        long years = months / 12;
        return years + " năm trước";
    }
}
