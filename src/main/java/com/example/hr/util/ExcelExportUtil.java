package com.example.hr.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Utility xuất Excel dùng Apache POI.
 */
public final class ExcelExportUtil {

    private ExcelExportUtil() {}

    /**
     * Tạo workbook Excel từ dữ liệu tabular.
     */
    public static byte[] exportToExcel(String sheetName, List<String> headers,
                                         List<List<Object>> data) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            // Header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Data style
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Create data rows
            for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                List<Object> rowData = data.get(rowIndex);

                for (int colIndex = 0; colIndex < rowData.size(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object value = rowData.get(colIndex);
                    setCellValue(cell, value, dataStyle, currencyStyle, dateStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Tạo workbook Excel từ Map data (key=column name, value=list of values).
     */
    public static byte[] exportMapToExcel(String sheetName, Map<String, List<Object>> data) throws IOException {
        List<String> headers = data.keySet().stream().toList();
        int maxRows = data.values().stream().mapToInt(List::size).max().orElse(0);

        List<List<Object>> rows = new java.util.ArrayList<>();
        for (int i = 0; i < maxRows; i++) {
            List<Object> row = new java.util.ArrayList<>();
            for (String header : headers) {
                List<Object> colData = data.get(header);
                row.add(i < colData.size() ? colData.get(i) : "");
            }
            rows.add(row);
        }

        return exportToExcel(sheetName, headers, rows);
    }

    // --- Style helpers ---

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("#,##0"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("dd/mm/yyyy"));
        return style;
    }

    private static void setCellValue(Cell cell, Object value, CellStyle dataStyle,
                                       CellStyle currencyStyle, CellStyle dateStyle) {
        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(dataStyle);
        } else if (value instanceof BigDecimal bd) {
            cell.setCellValue(bd.doubleValue());
            cell.setCellStyle(currencyStyle);
        } else if (value instanceof Number num) {
            cell.setCellValue(num.doubleValue());
            cell.setCellStyle(dataStyle);
        } else if (value instanceof LocalDate ld) {
            cell.setCellValue(ld.toString());
            cell.setCellStyle(dateStyle);
        } else if (value instanceof LocalDateTime ldt) {
            cell.setCellValue(ldt.toString());
            cell.setCellStyle(dateStyle);
        } else if (value instanceof Boolean bool) {
            cell.setCellValue(bool ? "Có" : "Không");
            cell.setCellStyle(dataStyle);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellStyle(dataStyle);
        }
    }
}
