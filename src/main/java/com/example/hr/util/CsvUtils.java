package com.example.hr.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSV Utility Class
 * Cung cấp các phương thức xử lý CSV files
 */
@Slf4j
public class CsvUtils {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final String DEFAULT_QUOTE = "\"";

    /**
     * Read CSV file to List of String arrays
     */
    public static List<String[]> readCsv(String filePath) {
        return readCsv(filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Read CSV file with custom separator
     */
    public static List<String[]> readCsv(String filePath, String separator) {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = parseCsvLine(line, separator);
                records.add(values);
            }
            
        } catch (IOException e) {
            log.error("Error reading CSV file: {}", filePath, e);
        }
        
        return records;
    }

    /**
     * Write List of String arrays to CSV file
     */
    public static void writeCsv(String filePath, List<String[]> records) {
        writeCsv(filePath, records, DEFAULT_SEPARATOR);
    }

    /**
     * Write List of String arrays to CSV file with custom separator
     */
    public static void writeCsv(String filePath, List<String[]> records, String separator) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            
            for (String[] record : records) {
                String line = formatCsvLine(record, separator);
                bw.write(line);
                bw.newLine();
            }
            
        } catch (IOException e) {
            log.error("Error writing CSV file: {}", filePath, e);
        }
    }

    /**
     * Parse CSV line considering quotes
     */
    private static String[] parseCsvLine(String line, String separator) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == separator.charAt(0) && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        
        values.add(currentValue.toString().trim());
        return values.toArray(new String[0]);
    }

    /**
     * Format CSV line with proper quoting
     */
    private static String formatCsvLine(String[] values, String separator) {
        return Arrays.stream(values)
                .map(value -> {
                    if (value.contains(separator) || value.contains("\"") || value.contains("\n")) {
                        return "\"" + value.replace("\"", "\"\"") + "\"";
                    }
                    return value;
                })
                .collect(Collectors.joining(separator));
    }

    /**
     * Convert CSV to List of Maps (first row as headers)
     */
    public static List<java.util.Map<String, String>> csvToMapList(String filePath) {
        List<String[]> records = readCsv(filePath);
        
        if (records.isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] headers = records.get(0);
        List<java.util.Map<String, String>> result = new ArrayList<>();
        
        for (int i = 1; i < records.size(); i++) {
            String[] values = records.get(i);
            java.util.Map<String, String> map = new java.util.HashMap<>();
            
            for (int j = 0; j < headers.length && j < values.length; j++) {
                map.put(headers[j], values[j]);
            }
            
            result.add(map);
        }
        
        return result;
    }

    /**
     * Append row to existing CSV file
     */
    public static void appendCsvRow(String filePath, String[] row) {
        appendCsvRow(filePath, row, DEFAULT_SEPARATOR);
    }

    /**
     * Append row to existing CSV file with custom separator
     */
    public static void appendCsvRow(String filePath, String[] row, String separator) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            
            String line = formatCsvLine(row, separator);
            bw.write(line);
            bw.newLine();
            
        } catch (IOException e) {
            log.error("Error appending to CSV file: {}", filePath, e);
        }
    }

    /**
     * Count rows in CSV file
     */
    public static int countRows(String filePath) {
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            while (br.readLine() != null) {
                count++;
            }
            
        } catch (IOException e) {
            log.error("Error counting rows in CSV file: {}", filePath, e);
        }
        
        return count;
    }

    /**
     * Get CSV headers (first row)
     */
    public static String[] getHeaders(String filePath) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            String line = br.readLine();
            if (line != null) {
                return parseCsvLine(line, DEFAULT_SEPARATOR);
            }
            
        } catch (IOException e) {
            log.error("Error reading CSV headers: {}", filePath, e);
        }
        
        return new String[0];
    }

    /**
     * Filter CSV rows by column value
     */
    public static List<String[]> filterByColumn(String filePath, int columnIndex, String value) {
        List<String[]> records = readCsv(filePath);
        List<String[]> filtered = new ArrayList<>();
        
        for (String[] record : records) {
            if (columnIndex < record.length && record[columnIndex].equals(value)) {
                filtered.add(record);
            }
        }
        
        return filtered;
    }

    /**
     * Sort CSV by column
     */
    public static List<String[]> sortByColumn(String filePath, int columnIndex, boolean ascending) {
        List<String[]> records = readCsv(filePath);
        
        if (records.size() <= 1) {
            return records;
        }
        
        String[] headers = records.get(0);
        List<String[]> data = records.subList(1, records.size());
        
        data.sort((a, b) -> {
            if (columnIndex >= a.length || columnIndex >= b.length) {
                return 0;
            }
            
            int comparison = a[columnIndex].compareTo(b[columnIndex]);
            return ascending ? comparison : -comparison;
        });
        
        List<String[]> result = new ArrayList<>();
        result.add(headers);
        result.addAll(data);
        
        return result;
    }

    /**
     * Merge multiple CSV files
     */
    public static void mergeCsvFiles(List<String> inputFiles, String outputFile) {
        List<String[]> allRecords = new ArrayList<>();
        boolean headersAdded = false;
        
        for (String inputFile : inputFiles) {
            List<String[]> records = readCsv(inputFile);
            
            if (!records.isEmpty()) {
                if (!headersAdded) {
                    allRecords.add(records.get(0));
                    headersAdded = true;
                }
                
                allRecords.addAll(records.subList(1, records.size()));
            }
        }
        
        writeCsv(outputFile, allRecords);
    }

    /**
     * Convert CSV to TSV (Tab-Separated Values)
     */
    public static void csvToTsv(String csvFile, String tsvFile) {
        List<String[]> records = readCsv(csvFile, DEFAULT_SEPARATOR);
        writeCsv(tsvFile, records, "\t");
    }

    /**
     * Remove duplicates from CSV
     */
    public static void removeDuplicates(String inputFile, String outputFile) {
        List<String[]> records = readCsv(inputFile);
        
        if (records.isEmpty()) {
            return;
        }
        
        String[] headers = records.get(0);
        List<String[]> uniqueRecords = new ArrayList<>();
        uniqueRecords.add(headers);
        
        java.util.Set<String> seen = new java.util.HashSet<>();
        
        for (int i = 1; i < records.size(); i++) {
            String key = String.join("|", records.get(i));
            if (seen.add(key)) {
                uniqueRecords.add(records.get(i));
            }
        }
        
        writeCsv(outputFile, uniqueRecords);
    }
}
