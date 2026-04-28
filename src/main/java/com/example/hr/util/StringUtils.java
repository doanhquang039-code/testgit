package com.example.hr.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for string operations
 */
public class StringUtils {

    /**
     * Convert Vietnamese string to slug (URL-friendly)
     * Example: "Nguyễn Văn A" -> "nguyen-van-a"
     */
    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = normalized.replaceAll("\\p{M}", "");
        
        return withoutAccents
                .toLowerCase()
                .replaceAll("[đĐ]", "d")
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    /**
     * Remove Vietnamese accents
     */
    public static String removeAccents(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized
                .replaceAll("\\p{M}", "")
                .replaceAll("[đĐ]", "d");
    }

    /**
     * Capitalize first letter of each word
     */
    public static String capitalizeWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        return Arrays.stream(input.trim().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    /**
     * Truncate string to specified length with ellipsis
     */
    public static String truncate(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        }
        return input.substring(0, maxLength - 3) + "...";
    }

    /**
     * Mask email address
     * Example: "john.doe@example.com" -> "j***e@example.com"
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return username + "@" + domain;
        }
        
        return username.charAt(0) + "***" + username.charAt(username.length() - 1) + "@" + domain;
    }

    /**
     * Mask phone number
     * Example: "0123456789" -> "012***6789"
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        
        int visibleDigits = 3;
        int maskedLength = phone.length() - (visibleDigits * 2);
        
        return phone.substring(0, visibleDigits) + 
               "*".repeat(maskedLength) + 
               phone.substring(phone.length() - visibleDigits);
    }

    /**
     * Mask citizen ID
     * Example: "123456789" -> "123***789"
     */
    public static String maskCitizenId(String citizenId) {
        if (citizenId == null || citizenId.length() < 6) {
            return citizenId;
        }
        
        return citizenId.substring(0, 3) + "***" + citizenId.substring(citizenId.length() - 3);
    }

    /**
     * Generate random string
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }

    /**
     * Generate random numeric string
     */
    public static String generateRandomNumeric(int length) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        
        return sb.toString();
    }

    /**
     * Check if string contains only letters
     */
    public static boolean isAlpha(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("[a-zA-Z]+");
    }

    /**
     * Check if string contains only letters and numbers
     */
    public static boolean isAlphanumeric(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.matches("[a-zA-Z0-9]+");
    }

    /**
     * Reverse string
     */
    public static String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }

    /**
     * Count occurrences of substring
     */
    public static int countOccurrences(String input, String substring) {
        if (input == null || substring == null || substring.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        int index = 0;
        
        while ((index = input.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }

    /**
     * Join list of strings with delimiter
     */
    public static String join(List<String> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return String.join(delimiter, list);
    }

    /**
     * Split string and trim each part
     */
    public static List<String> splitAndTrim(String input, String delimiter) {
        if (input == null || input.isEmpty()) {
            return List.of();
        }
        
        return Arrays.stream(input.split(delimiter))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Pad string to left with specified character
     */
    public static String padLeft(String input, int length, char padChar) {
        if (input == null) {
            input = "";
        }
        
        if (input.length() >= length) {
            return input;
        }
        
        return String.valueOf(padChar).repeat(length - input.length()) + input;
    }

    /**
     * Pad string to right with specified character
     */
    public static String padRight(String input, int length, char padChar) {
        if (input == null) {
            input = "";
        }
        
        if (input.length() >= length) {
            return input;
        }
        
        return input + String.valueOf(padChar).repeat(length - input.length());
    }

    /**
     * Extract initials from full name
     * Example: "Nguyễn Văn A" -> "NVA"
     */
    public static String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        
        return Arrays.stream(fullName.trim().split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase())
                .collect(Collectors.joining());
    }

    /**
     * Format currency (Vietnamese Dong)
     */
    public static String formatCurrency(Double amount) {
        if (amount == null) {
            return "0 VND";
        }
        return String.format("%,.0f VND", amount);
    }

    /**
     * Format file size
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }

    /**
     * Escape HTML special characters
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * Generate employee code
     * Example: "NV001234"
     */
    public static String generateEmployeeCode(int sequence) {
        return "NV" + padLeft(String.valueOf(sequence), 6, '0');
    }

    /**
     * Generate contract code
     * Example: "HD2024001234"
     */
    public static String generateContractCode(int year, int sequence) {
        return "HD" + year + padLeft(String.valueOf(sequence), 6, '0');
    }

    /**
     * Check if two strings are equal (case-insensitive)
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    /**
     * Get file extension from filename
     */
    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * Remove file extension from filename
     */
    public static String removeFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return filename;
        }
        return filename.substring(0, filename.lastIndexOf("."));
    }
}
