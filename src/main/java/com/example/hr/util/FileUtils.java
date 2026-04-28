package com.example.hr.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for file operations
 */
public class FileUtils {

    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    /**
     * Get file extension from filename
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Get filename without extension
     */
    public static String getFileNameWithoutExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return filename;
        }
        return filename.substring(0, lastDotIndex);
    }

    /**
     * Format file size to human readable format
     */
    public static String formatFileSize(long size) {
        if (size < 0) {
            return "Invalid size";
        }
        if (size < KB) {
            return size + " B";
        } else if (size < MB) {
            return DECIMAL_FORMAT.format((double) size / KB) + " KB";
        } else if (size < GB) {
            return DECIMAL_FORMAT.format((double) size / MB) + " MB";
        } else {
            return DECIMAL_FORMAT.format((double) size / GB) + " GB";
        }
    }

    /**
     * Check if file is an image
     */
    public static boolean isImageFile(String filename) {
        String extension = getFileExtension(filename);
        Set<String> imageExtensions = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "svg");
        return imageExtensions.contains(extension);
    }

    /**
     * Check if file is a document
     */
    public static boolean isDocumentFile(String filename) {
        String extension = getFileExtension(filename);
        Set<String> docExtensions = Set.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv");
        return docExtensions.contains(extension);
    }

    /**
     * Check if file is a video
     */
    public static boolean isVideoFile(String filename) {
        String extension = getFileExtension(filename);
        Set<String> videoExtensions = Set.of("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm");
        return videoExtensions.contains(extension);
    }

    /**
     * Check if file is an audio
     */
    public static boolean isAudioFile(String filename) {
        String extension = getFileExtension(filename);
        Set<String> audioExtensions = Set.of("mp3", "wav", "ogg", "flac", "aac", "m4a");
        return audioExtensions.contains(extension);
    }

    /**
     * Generate unique filename with timestamp
     */
    public static String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String nameWithoutExt = getFileNameWithoutExtension(originalFilename);
        String timestamp = String.valueOf(System.currentTimeMillis());
        return nameWithoutExt + "_" + timestamp + (extension.isEmpty() ? "" : "." + extension);
    }

    /**
     * Calculate MD5 hash of file
     */
    public static String calculateMD5(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // Read file
            }
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Calculate SHA-256 hash of file
     */
    public static String calculateSHA256(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream is = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // Read file
            }
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Copy file from source to destination
     */
    public static void copyFile(File source, File destination) throws IOException {
        if (!source.exists()) {
            throw new FileNotFoundException("Source file not found: " + source.getAbsolutePath());
        }
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Move file from source to destination
     */
    public static void moveFile(File source, File destination) throws IOException {
        if (!source.exists()) {
            throw new FileNotFoundException("Source file not found: " + source.getAbsolutePath());
        }
        Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Delete file or directory recursively
     */
    public static boolean deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    deleteRecursively(child);
                }
            }
        }
        return file.delete();
    }

    /**
     * Create directory if not exists
     */
    public static void createDirectoryIfNotExists(String path) throws IOException {
        Path dirPath = Paths.get(path);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    /**
     * List all files in directory
     */
    public static List<File> listFiles(File directory, boolean recursive) {
        List<File> fileList = new ArrayList<>();
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return fileList;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileList.add(file);
                } else if (file.isDirectory() && recursive) {
                    fileList.addAll(listFiles(file, true));
                }
            }
        }
        return fileList;
    }

    /**
     * List files with specific extension
     */
    public static List<File> listFilesByExtension(File directory, String extension, boolean recursive) {
        return listFiles(directory, recursive).stream()
                .filter(file -> getFileExtension(file.getName()).equals(extension.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get total size of directory
     */
    public static long getDirectorySize(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        long size = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += getDirectorySize(file);
                }
            }
        }
        return size;
    }

    /**
     * Zip files into archive
     */
    public static void zipFiles(List<File> files, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (File file : files) {
                if (file.isFile()) {
                    addFileToZip(file, file.getName(), zos);
                }
            }
        }
    }

    /**
     * Add file to zip archive
     */
    private static void addFileToZip(File file, String fileName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    /**
     * Unzip archive to directory
     */
    public static void unzipFile(File zipFile, File destDirectory) throws IOException {
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDirectory, entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[8192];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    /**
     * Read file content as string
     */
    public static String readFileAsString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    /**
     * Write string to file
     */
    public static void writeStringToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes());
    }

    /**
     * Append string to file
     */
    public static void appendStringToFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * Check if MultipartFile is valid
     */
    public static boolean isValidMultipartFile(MultipartFile file) {
        return file != null && !file.isEmpty() && file.getOriginalFilename() != null;
    }

    /**
     * Save MultipartFile to destination
     */
    public static void saveMultipartFile(MultipartFile file, File destination) throws IOException {
        if (!isValidMultipartFile(file)) {
            throw new IllegalArgumentException("Invalid multipart file");
        }
        try (InputStream is = file.getInputStream();
             OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    /**
     * Get MIME type from file extension
     */
    public static String getMimeType(String filename) {
        String extension = getFileExtension(filename);
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("doc", "application/msword");
        mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put("xls", "application/vnd.ms-excel");
        mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("csv", "text/csv");
        mimeTypes.put("mp4", "video/mp4");
        mimeTypes.put("mp3", "audio/mpeg");
        return mimeTypes.getOrDefault(extension, "application/octet-stream");
    }

    /**
     * Sanitize filename to remove invalid characters
     */
    public static String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "unnamed";
        }
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    /**
     * Check if file exists and is readable
     */
    public static boolean isFileReadable(File file) {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Check if file exists and is writable
     */
    public static boolean isFileWritable(File file) {
        return file != null && file.exists() && file.isFile() && file.canWrite();
    }

    /**
     * Get file creation time
     */
    public static long getFileCreationTime(File file) throws IOException {
        if (!isFileReadable(file)) {
            throw new FileNotFoundException("File not found or not readable");
        }
        return Files.readAttributes(file.toPath(), java.nio.file.attribute.BasicFileAttributes.class)
                .creationTime().toMillis();
    }

    /**
     * Get file last modified time
     */
    public static long getFileLastModifiedTime(File file) {
        if (!isFileReadable(file)) {
            return 0;
        }
        return file.lastModified();
    }

    /**
     * Compare two files by content
     */
    public static boolean areFilesEqual(File file1, File file2) throws IOException {
        if (!isFileReadable(file1) || !isFileReadable(file2)) {
            return false;
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        try {
            return calculateMD5(file1).equals(calculateMD5(file2));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("MD5 algorithm not available", e);
        }
    }

    private static class DigestInputStream extends FilterInputStream {
        private final MessageDigest digest;

        protected DigestInputStream(InputStream in, MessageDigest digest) {
            super(in);
            this.digest = digest;
        }

        @Override
        public int read() throws IOException {
            int b = in.read();
            if (b != -1) {
                digest.update((byte) b);
            }
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int result = in.read(b, off, len);
            if (result != -1) {
                digest.update(b, off, result);
            }
            return result;
        }
    }
}
