package com.example.hr.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for date and time operations
 */
public class DateTimeUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    /**
     * Get current date in Vietnam timezone
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now(VIETNAM_ZONE);
    }

    /**
     * Get current datetime in Vietnam timezone
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(VIETNAM_ZONE);
    }

    /**
     * Format date to dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }

    /**
     * Format datetime to dd/MM/yyyy HH:mm:ss
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Format time to HH:mm
     */
    public static String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.format(TIME_FORMATTER);
    }

    /**
     * Parse date from string dd/MM/yyyy
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * Parse datetime from string dd/MM/yyyy HH:mm:ss
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) return null;
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculate hours between two datetimes
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * Calculate minutes between two datetimes
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) return 0;
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * Get first day of current month
     */
    public static LocalDate getFirstDayOfMonth() {
        return getCurrentDate().with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Get last day of current month
     */
    public static LocalDate getLastDayOfMonth() {
        return getCurrentDate().with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Get first day of given month
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Get last day of given month
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Check if date is weekend (Saturday or Sunday)
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) return false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        if (date == null) return false;
        return date.equals(getCurrentDate());
    }

    /**
     * Check if datetime is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isBefore(getCurrentDateTime());
    }

    /**
     * Check if datetime is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isAfter(getCurrentDateTime());
    }

    /**
     * Get all dates between start and end date (inclusive)
     */
    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        if (startDate == null || endDate == null) return dates;
        
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            dates.add(current);
            current = current.plusDays(1);
        }
        return dates;
    }

    /**
     * Get working days between start and end date (excluding weekends)
     */
    public static long getWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return 0;
        
        long workingDays = 0;
        LocalDate current = startDate;
        
        while (!current.isAfter(endDate)) {
            if (!isWeekend(current)) {
                workingDays++;
            }
            current = current.plusDays(1);
        }
        
        return workingDays;
    }

    /**
     * Add working days to a date (excluding weekends)
     */
    public static LocalDate addWorkingDays(LocalDate date, int days) {
        if (date == null) return null;
        
        LocalDate result = date;
        int addedDays = 0;
        
        while (addedDays < days) {
            result = result.plusDays(1);
            if (!isWeekend(result)) {
                addedDays++;
            }
        }
        
        return result;
    }

    /**
     * Get age from birth date
     */
    public static int getAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, getCurrentDate()).getYears();
    }

    /**
     * Get relative time string (e.g., "2 hours ago", "3 days ago")
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        
        LocalDateTime now = getCurrentDateTime();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        
        if (minutes < 1) return "Vừa xong";
        if (minutes < 60) return minutes + " phút trước";
        
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        if (hours < 24) return hours + " giờ trước";
        
        long days = ChronoUnit.DAYS.between(dateTime, now);
        if (days < 7) return days + " ngày trước";
        
        long weeks = ChronoUnit.WEEKS.between(dateTime, now);
        if (weeks < 4) return weeks + " tuần trước";
        
        long months = ChronoUnit.MONTHS.between(dateTime, now);
        if (months < 12) return months + " tháng trước";
        
        long years = ChronoUnit.YEARS.between(dateTime, now);
        return years + " năm trước";
    }

    /**
     * Get month name in Vietnamese
     */
    public static String getVietnameseMonthName(int month) {
        String[] months = {
            "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
            "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
            "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };
        return months[month - 1];
    }

    /**
     * Get day of week name in Vietnamese
     */
    public static String getVietnameseDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "Thứ Hai";
            case TUESDAY: return "Thứ Ba";
            case WEDNESDAY: return "Thứ Tư";
            case THURSDAY: return "Thứ Năm";
            case FRIDAY: return "Thứ Sáu";
            case SATURDAY: return "Thứ Bảy";
            case SUNDAY: return "Chủ Nhật";
            default: return "";
        }
    }

    /**
     * Get quarter of year (1-4)
     */
    public static int getQuarter(LocalDate date) {
        if (date == null) return 0;
        return (date.getMonthValue() - 1) / 3 + 1;
    }

    /**
     * Get first day of quarter
     */
    public static LocalDate getFirstDayOfQuarter(LocalDate date) {
        if (date == null) return null;
        int quarter = getQuarter(date);
        int firstMonthOfQuarter = (quarter - 1) * 3 + 1;
        return LocalDate.of(date.getYear(), firstMonthOfQuarter, 1);
    }

    /**
     * Get last day of quarter
     */
    public static LocalDate getLastDayOfQuarter(LocalDate date) {
        if (date == null) return null;
        int quarter = getQuarter(date);
        int lastMonthOfQuarter = quarter * 3;
        return LocalDate.of(date.getYear(), lastMonthOfQuarter, 1)
                .with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Convert LocalDateTime to timestamp (milliseconds)
     */
    public static long toTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) return 0;
        return dateTime.atZone(VIETNAM_ZONE).toInstant().toEpochMilli();
    }

    /**
     * Convert timestamp to LocalDateTime
     */
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), VIETNAM_ZONE);
    }

    /**
     * Check if two date ranges overlap
     */
    public static boolean isDateRangeOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null) return false;
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    /**
     * Get fiscal year (assuming fiscal year starts in April)
     */
    public static int getFiscalYear(LocalDate date) {
        if (date == null) return 0;
        return date.getMonthValue() >= 4 ? date.getYear() : date.getYear() - 1;
    }
}
