package com.example.hr.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^(\\+84|0)[0-9]{9,10}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._-]{3,20}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    private static final Pattern CITIZEN_ID_PATTERN = Pattern.compile(
        "^[0-9]{9}$|^[0-9]{12}$"
    );

    /**
     * Validate email address
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate Vietnamese phone number
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    /**
     * Validate username
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validate strong password
     * - At least 8 characters
     * - Contains at least one digit
     * - Contains at least one lowercase letter
     * - Contains at least one uppercase letter
     * - Contains at least one special character
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validate Vietnamese citizen ID (CMND/CCCD)
     * - 9 digits (old CMND) or 12 digits (new CCCD)
     */
    public static boolean isValidCitizenId(String citizenId) {
        if (citizenId == null || citizenId.trim().isEmpty()) {
            return false;
        }
        return CITIZEN_ID_PATTERN.matcher(citizenId.trim()).matches();
    }

    /**
     * Validate salary (must be positive)
     */
    public static boolean isValidSalary(Double salary) {
        return salary != null && salary > 0;
    }

    /**
     * Validate percentage (0-100)
     */
    public static boolean isValidPercentage(Double percentage) {
        return percentage != null && percentage >= 0 && percentage <= 100;
    }

    /**
     * Validate rating (1-5)
     */
    public static boolean isValidRating(Integer rating) {
        return rating != null && rating >= 1 && rating <= 5;
    }

    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Validate string length
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) return false;
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate numeric string
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate integer string
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate URL
     */
    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) return false;
        try {
            new java.net.URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sanitize string (remove special characters)
     */
    public static String sanitize(String str) {
        if (str == null) return "";
        return str.replaceAll("[<>\"'&]", "");
    }

    /**
     * Validate Vietnamese name (only letters and spaces)
     */
    public static boolean isValidVietnameseName(String name) {
        if (isEmpty(name)) return false;
        return name.matches("^[a-zA-ZÀ-ỹ\\s]+$");
    }

    /**
     * Validate bank account number
     */
    public static boolean isValidBankAccount(String accountNumber) {
        if (isEmpty(accountNumber)) return false;
        return accountNumber.matches("^[0-9]{9,14}$");
    }

    /**
     * Validate tax code (MST)
     */
    public static boolean isValidTaxCode(String taxCode) {
        if (isEmpty(taxCode)) return false;
        return taxCode.matches("^[0-9]{10}(-[0-9]{3})?$");
    }

    /**
     * Get validation error message for email
     */
    public static String getEmailErrorMessage() {
        return "Email không hợp lệ. Vui lòng nhập đúng định dạng email.";
    }

    /**
     * Get validation error message for phone
     */
    public static String getPhoneErrorMessage() {
        return "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam (10-11 số).";
    }

    /**
     * Get validation error message for username
     */
    public static String getUsernameErrorMessage() {
        return "Tên đăng nhập không hợp lệ. Chỉ chấp nhận chữ cái, số, dấu chấm, gạch dưới và gạch ngang (3-20 ký tự).";
    }

    /**
     * Get validation error message for password
     */
    public static String getPasswordErrorMessage() {
        return "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";
    }

    /**
     * Get validation error message for citizen ID
     */
    public static String getCitizenIdErrorMessage() {
        return "Số CMND/CCCD không hợp lệ. Vui lòng nhập 9 số (CMND) hoặc 12 số (CCCD).";
    }
}
