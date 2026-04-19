package com.example.hr.util;

/**
 * Utility validation chung.
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * Validate email format.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Validate số điện thoại VN.
     */
    public static boolean isValidVietnamesePhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return phone.matches("^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-6|8|9]|9[0-4|6-9])[0-9]{7}$");
    }

    /**
     * Validate CMND/CCCD (9 hoặc 12 số).
     */
    public static boolean isValidIdentityCard(String number) {
        if (number == null || number.isBlank()) return false;
        return number.matches("^[0-9]{9}$") || number.matches("^[0-9]{12}$");
    }

    /**
     * Validate mã số thuế (10 hoặc 13 số).
     */
    public static boolean isValidTaxCode(String code) {
        if (code == null || code.isBlank()) return false;
        return code.matches("^[0-9]{10}$") || code.matches("^[0-9]{10}-[0-9]{3}$");
    }

    /**
     * Sanitize input string (prevent XSS).
     */
    public static String sanitize(String input) {
        if (input == null) return null;
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * Truncate string to max length.
     */
    public static String truncate(String input, int maxLength) {
        if (input == null) return null;
        if (input.length() <= maxLength) return input;
        return input.substring(0, maxLength - 3) + "...";
    }

    /**
     * Generate slug from Vietnamese text.
     */
    public static String generateSlug(String input) {
        if (input == null || input.isBlank()) return "";
        return input.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăắằặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("đ", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("[\\s]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    /**
     * Format tên (capitalize first letter of each word).
     */
    public static String formatFullName(String name) {
        if (name == null || name.isBlank()) return name;
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(Character.toUpperCase(part.charAt(0)))
              .append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }
}
