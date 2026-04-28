package com.example.hr.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Utility class for Excel operations using Apache POI
 */
public class ExcelUtils {

    /**
     * Create new workbook
     */
    public static Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    /**
     * Create new sheet in workbook
     */
    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * Create header row
     */
    public static void createHeaderRow(Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * Create header style
     */
    public static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    /**
     * Create data style
     */
    public static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Create date style
     */
    public static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        return style;
    }

    /**
     * Create datetime style
     */
    public static CellStyle createDateTimeStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
        return style;
    }

    /**
     * Create currency style
     */
    public static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    /**
     * Create percentage style
     */
    public static CellStyle createPercentageStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("0.00%"));
        return style;
    }

    /**
     * Set cell value with auto type detection
     */
    public static void setCellValue(Cell cell, Object value, CellStyle style) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
            cell.setCellValue(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else if (value instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) value;
            cell.setCellValue(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        } else {
            cell.setCellValue(value.toString());
        }
        
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    /**
     * Auto-size columns
     */
    public static void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Write workbook to file
     */
    public static void writeToFile(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
    }

    /**
     * Write workbook to output stream
     */
    public static void writeToStream(Workbook workbook, OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    /**
     * Read workbook from file
     */
    public static Workbook readFromFile(String filePath) throws IOException {
        try (FileInputStream fileIn = new FileInputStream(filePath)) {
            return WorkbookFactory.create(fileIn);
        }
    }

    /**
     * Read workbook from input stream
     */
    public static Workbook readFromStream(InputStream inputStream) throws IOException {
        return WorkbookFactory.create(inputStream);
    }

    /**
     * Get cell value as string
     */
    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get cell value as integer
     */
    public static Integer getCellValueAsInteger(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Integer.parseInt(cell.getStringCellValue());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Get cell value as double
     */
    public static Double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Double.parseDouble(cell.getStringCellValue());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Get cell value as date
     */
    public static Date getCellValueAsDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Get cell value as boolean
     */
    public static Boolean getCellValueAsBoolean(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        try {
            if (cell.getCellType() == CellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                return Boolean.parseBoolean(cell.getStringCellValue());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Read all rows from sheet
     */
    public static List<List<String>> readAllRows(Sheet sheet) {
        List<List<String>> rows = new ArrayList<>();
        
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(getCellValueAsString(cell));
            }
            rows.add(rowData);
        }
        
        return rows;
    }

    /**
     * Read rows as map (first row as keys)
     */
    public static List<Map<String, String>> readRowsAsMap(Sheet sheet) {
        List<Map<String, String>> result = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        
        if (!rowIterator.hasNext()) {
            return result;
        }
        
        // Read header row
        Row headerRow = rowIterator.next();
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(getCellValueAsString(cell));
        }
        
        // Read data rows
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> rowMap = new HashMap<>();
            
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.getCell(i);
                rowMap.put(headers.get(i), getCellValueAsString(cell));
            }
            
            result.add(rowMap);
        }
        
        return result;
    }

    /**
     * Export list of maps to Excel
     */
    public static void exportToExcel(List<Map<String, Object>> data, String[] headers, String filePath) throws IOException {
        Workbook workbook = createWorkbook();
        Sheet sheet = createSheet(workbook, "Data");
        
        // Create header row
        createHeaderRow(sheet, headers);
        
        // Create data rows
        CellStyle dataStyle = createDataStyle(workbook);
        int rowNum = 1;
        
        for (Map<String, Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            
            for (String header : headers) {
                Cell cell = row.createCell(colNum++);
                Object value = rowData.get(header);
                setCellValue(cell, value, dataStyle);
            }
        }
        
        // Auto-size columns
        autoSizeColumns(sheet, headers.length);
        
        // Write to file
        writeToFile(workbook, filePath);
        workbook.close();
    }

    /**
     * Merge cells
     */
    public static void mergeCells(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * Set column width
     */
    public static void setColumnWidth(Sheet sheet, int columnIndex, int width) {
        sheet.setColumnWidth(columnIndex, width * 256);
    }

    /**
     * Freeze panes
     */
    public static void freezePanes(Sheet sheet, int colSplit, int rowSplit) {
        sheet.createFreezePane(colSplit, rowSplit);
    }

    /**
     * Add filter to header row
     */
    public static void addAutoFilter(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * Close workbook safely
     */
    public static void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                // Log error
            }
        }
    }
}
