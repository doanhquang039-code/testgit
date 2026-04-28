package com.example.hr.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler cho toàn bộ ứng dụng.
 * Xử lý cả API (JSON) và MVC (Thymeleaf) exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Xử lý ResourceNotFoundException.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 404);
        mav.addObject("error", "Không tìm thấy");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(HttpStatus.NOT_FOUND);
        return mav;
    }

    /**
     * Xử lý BusinessValidationException.
     */
    @ExceptionHandler(BusinessValidationException.class)
    public Object handleBusinessValidation(BusinessValidationException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), request.getRequestURI());
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 422);
        mav.addObject("error", "Lỗi nghiệp vụ");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        return mav;
    }

    /**
     * Xử lý DuplicateResourceException.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex,
                                                                        HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý UnauthorizedAccessException.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public Object handleUnauthorizedAccess(UnauthorizedAccessException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 403);
        mav.addObject("error", "Không có quyền truy cập");
        mav.addObject("message", ex.getMessage());
        mav.setStatus(HttpStatus.FORBIDDEN);
        return mav;
    }

    /**
     * Xử lý FileUploadException.
     */
    @ExceptionHandler(FileUploadException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFileUpload(FileUploadException ex,
                                                                  HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý PayrollCalculationException.
     */
    @ExceptionHandler(PayrollCalculationException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handlePayrollCalculation(PayrollCalculationException ex,
                                                                          HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý ApprovalWorkflowException.
     */
    @ExceptionHandler(ApprovalWorkflowException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleApprovalWorkflow(ApprovalWorkflowException ex,
                                                                        HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý validation errors (Bean Validation).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "Dữ liệu không hợp lệ");
        body.put("fieldErrors", fieldErrors);
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Catch-all handler cho unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request) {
        // Log chi tiết exception để debug
        logger.error("Unexpected exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        
        if (isApiRequest(request)) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Đã xảy ra lỗi hệ thống: " + ex.getMessage(), request.getRequestURI());
        }
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 500);
        mav.addObject("error", "Lỗi hệ thống");
        mav.addObject("message", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.");
        mav.addObject("details", ex.getMessage()); // Thêm chi tiết lỗi
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }

    // --- Helper Methods ---

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader("Accept");
        return uri.startsWith("/api/")
                || (accept != null && accept.contains("application/json"));
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }
}
