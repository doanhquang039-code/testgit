package com.example.hr.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * PDF Utility Class
 * Cung cấp các phương thức tạo và xử lý PDF
 */
@Slf4j
public class PdfUtils {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

    /**
     * Create simple PDF document
     */
    public static byte[] createSimplePdf(String title, String content) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Add title
            Paragraph titlePara = new Paragraph(title, TITLE_FONT);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);
            
            // Add content
            Paragraph contentPara = new Paragraph(content, NORMAL_FONT);
            contentPara.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(contentPara);
            
            document.close();
            
        } catch (DocumentException e) {
            log.error("Error creating PDF", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Create PDF with table
     */
    public static byte[] createPdfWithTable(String title, String[] headers, List<String[]> data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Add title
            Paragraph titlePara = new Paragraph(title, TITLE_FONT);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);
            
            // Create table
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Add headers
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }
            
            // Add data
            for (String[] row : data) {
                for (String value : row) {
                    PdfPCell cell = new PdfPCell(new Phrase(value, NORMAL_FONT));
                    cell.setPadding(5);
                    table.addCell(cell);
                }
            }
            
            document.add(table);
            document.close();
            
        } catch (DocumentException e) {
            log.error("Error creating PDF with table", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Create PDF report with sections
     */
    public static byte[] createReport(String title, Map<String, String> sections) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Add title
            Paragraph titlePara = new Paragraph(title, TITLE_FONT);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(30);
            document.add(titlePara);
            
            // Add sections
            for (Map.Entry<String, String> entry : sections.entrySet()) {
                // Section header
                Paragraph headerPara = new Paragraph(entry.getKey(), HEADER_FONT);
                headerPara.setSpacingBefore(15);
                headerPara.setSpacingAfter(10);
                document.add(headerPara);
                
                // Section content
                Paragraph contentPara = new Paragraph(entry.getValue(), NORMAL_FONT);
                contentPara.setAlignment(Element.ALIGN_JUSTIFIED);
                contentPara.setSpacingAfter(10);
                document.add(contentPara);
            }
            
            document.close();
            
        } catch (DocumentException e) {
            log.error("Error creating PDF report", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Add watermark to PDF
     */
    public static byte[] addWatermark(byte[] pdfBytes, String watermarkText) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            PdfStamper stamper = new PdfStamper(reader, baos);
            
            Font watermarkFont = new Font(Font.FontFamily.HELVETICA, 52, Font.BOLD, BaseColor.LIGHT_GRAY);
            
            int totalPages = reader.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                PdfContentByte content = stamper.getOverContent(i);
                
                ColumnText.showTextAligned(content, Element.ALIGN_CENTER,
                        new Phrase(watermarkText, watermarkFont),
                        300, 400, 45);
            }
            
            stamper.close();
            reader.close();
            
        } catch (Exception e) {
            log.error("Error adding watermark to PDF", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Merge multiple PDFs
     */
    public static byte[] mergePdfs(List<byte[]> pdfBytesList) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, baos);
            document.open();
            
            for (byte[] pdfBytes : pdfBytesList) {
                PdfReader reader = new PdfReader(pdfBytes);
                int totalPages = reader.getNumberOfPages();
                
                for (int i = 1; i <= totalPages; i++) {
                    copy.addPage(copy.getImportedPage(reader, i));
                }
                
                reader.close();
            }
            
            document.close();
            
        } catch (Exception e) {
            log.error("Error merging PDFs", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Add page numbers to PDF
     */
    public static byte[] addPageNumbers(byte[] pdfBytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            PdfStamper stamper = new PdfStamper(reader, baos);
            
            int totalPages = reader.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                PdfContentByte content = stamper.getOverContent(i);
                
                ColumnText.showTextAligned(content, Element.ALIGN_CENTER,
                        new Phrase(String.format("Page %d of %d", i, totalPages), SMALL_FONT),
                        300, 30, 0);
            }
            
            stamper.close();
            reader.close();
            
        } catch (Exception e) {
            log.error("Error adding page numbers to PDF", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Create PDF with header and footer
     */
    public static byte[] createPdfWithHeaderFooter(String title, String content, 
                                                   String headerText, String footerText) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            // Set header and footer
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        // Header
                        PdfContentByte cb = writer.getDirectContent();
                        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                                new Phrase(headerText, SMALL_FONT),
                                300, 820, 0);
                        
                        // Footer
                        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                                new Phrase(footerText, SMALL_FONT),
                                300, 30, 0);
                    } catch (Exception e) {
                        log.error("Error adding header/footer", e);
                    }
                }
            });
            
            document.open();
            
            // Add title
            Paragraph titlePara = new Paragraph(title, TITLE_FONT);
            titlePara.setAlignment(Element.ALIGN_CENTER);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);
            
            // Add content
            Paragraph contentPara = new Paragraph(content, NORMAL_FONT);
            contentPara.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(contentPara);
            
            document.close();
            
        } catch (DocumentException e) {
            log.error("Error creating PDF with header/footer", e);
        }
        
        return baos.toByteArray();
    }

    /**
     * Save PDF to file
     */
    public static void savePdfToFile(byte[] pdfBytes, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        } catch (IOException e) {
            log.error("Error saving PDF to file: {}", filePath, e);
        }
    }

    /**
     * Get PDF page count
     */
    public static int getPageCount(byte[] pdfBytes) {
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            int pageCount = reader.getNumberOfPages();
            reader.close();
            return pageCount;
        } catch (IOException e) {
            log.error("Error getting PDF page count", e);
            return 0;
        }
    }

    /**
     * Extract text from PDF
     * Note: Basic text extraction - for advanced extraction, consider using Apache PDFBox
     */
    public static String extractText(byte[] pdfBytes) {
        StringBuilder text = new StringBuilder();
        
        try {
            PdfReader reader = new PdfReader(pdfBytes);
            int totalPages = reader.getNumberOfPages();
            
            for (int i = 1; i <= totalPages; i++) {
                // Basic text extraction using PdfReader
                byte[] pageContent = reader.getPageContent(i);
                if (pageContent != null) {
                    text.append(new String(pageContent));
                    text.append("\n");
                }
            }
            
            reader.close();
            
        } catch (IOException e) {
            log.error("Error extracting text from PDF", e);
        }
        
        return text.toString();
    }
}
