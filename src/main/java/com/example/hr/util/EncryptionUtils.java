package com.example.hr.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for encryption and hashing operations
 */
public class EncryptionUtils {

    private static final String AES_ALGORITHM = "AES";
    private static final String SHA_256 = "SHA-256";
    private static final String MD5 = "MD5";

    /**
     * Generate AES key
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    /**
     * Encrypt text using AES
     */
    public static String encryptAES(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypt text using AES
     */
    public static String decryptAES(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Hash text using SHA-256
     */
    public static String hashSHA256(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(SHA_256);
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * Hash text using MD5
     */
    public static String hashMD5(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(MD5);
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * Generate random salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash password with salt
     */
    public static String hashPasswordWithSalt(String password, String salt) throws NoSuchAlgorithmException {
        return hashSHA256(password + salt);
    }

    /**
     * Verify password with hash and salt
     */
    public static boolean verifyPassword(String password, String salt, String hash) throws NoSuchAlgorithmException {
        String computedHash = hashPasswordWithSalt(password, salt);
        return computedHash.equals(hash);
    }

    /**
     * Convert bytes to hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Generate random token
     */
    public static String generateRandomToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Encode to Base64
     */
    public static String encodeBase64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode from Base64
     */
    public static String decodeBase64(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Encode to URL-safe Base64
     */
    public static String encodeBase64Url(String text) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode from URL-safe Base64
     */
    public static String decodeBase64Url(String encodedText) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedText);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Create SecretKey from string
     */
    public static SecretKey createSecretKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES_ALGORITHM);
    }

    /**
     * Convert SecretKey to string
     */
    public static String secretKeyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Generate secure random number
     */
    public static int generateSecureRandomInt(int bound) {
        SecureRandom random = new SecureRandom();
        return random.nextInt(bound);
    }

    /**
     * Generate secure random long
     */
    public static long generateSecureRandomLong() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }

    /**
     * Generate OTP (One-Time Password)
     */
    public static String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Generate alphanumeric OTP
     */
    public static String generateAlphanumericOTP(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(chars.charAt(random.nextInt(chars.length())));
        }
        return otp.toString();
    }

    /**
     * Mask sensitive data (show only first and last N characters)
     */
    public static String maskSensitiveData(String data, int visibleChars) {
        if (data == null || data.length() <= visibleChars * 2) {
            return data;
        }
        String start = data.substring(0, visibleChars);
        String end = data.substring(data.length() - visibleChars);
        int maskedLength = data.length() - (visibleChars * 2);
        return start + "*".repeat(maskedLength) + end;
    }

    /**
     * Mask email address
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return username.charAt(0) + "***@" + domain;
        }
        
        return username.charAt(0) + "***" + username.charAt(username.length() - 1) + "@" + domain;
    }

    /**
     * Mask phone number
     */
    public static String maskPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 2);
    }

    /**
     * Mask credit card number
     */
    public static String maskCreditCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
