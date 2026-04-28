package com.example.hr.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for building API responses
 */
public class ResponseHelper {

    /**
     * Build success response
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Success");
    }

    /**
     * Build success response with message
     */
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Build created response
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(data, "Resource created successfully");
    }

    /**
     * Build created response with message
     */
    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(
                true,
                message,
                data,
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Build error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Build error response with status
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(
                false,
                message,
                null,
                null,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Build error response with errors map
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String message, Map<String, String> errors) {
        return error(message, errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Build error response with errors map and status
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String message, Map<String, String> errors, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(
                false,
                message,
                null,
                errors,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Build not found response
     */
    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message) {
        return error(message, HttpStatus.NOT_FOUND);
    }

    /**
     * Build unauthorized response
     */
    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Build forbidden response
     */
    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message) {
        return error(message, HttpStatus.FORBIDDEN);
    }

    /**
     * Build conflict response
     */
    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    /**
     * Build internal server error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> internalError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Build validation error response
     */
    public static <T> ResponseEntity<ApiResponse<T>> validationError(Map<String, String> errors) {
        return error("Validation failed", errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Build no content response
     */
    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Build paginated response
     */
    public static <T> ResponseEntity<PaginatedResponse<T>> paginated(
            java.util.List<T> data,
            int page,
            int size,
            long totalElements,
            int totalPages) {
        
        PaginatedResponse<T> response = new PaginatedResponse<>(
                true,
                "Success",
                data,
                page,
                size,
                totalElements,
                totalPages,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * API Response class
     */
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        private Map<String, String> errors;
        private LocalDateTime timestamp;

        public ApiResponse() {
        }

        public ApiResponse(boolean success, String message, T data, Map<String, String> errors, LocalDateTime timestamp) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.errors = errors;
            this.timestamp = timestamp;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Map<String, String> getErrors() {
            return errors;
        }

        public void setErrors(Map<String, String> errors) {
            this.errors = errors;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }

    /**
     * Paginated Response class
     */
    public static class PaginatedResponse<T> {
        private boolean success;
        private String message;
        private java.util.List<T> data;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private LocalDateTime timestamp;

        public PaginatedResponse() {
        }

        public PaginatedResponse(boolean success, String message, java.util.List<T> data, 
                                int page, int size, long totalElements, int totalPages, LocalDateTime timestamp) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.timestamp = timestamp;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public java.util.List<T> getData() {
            return data;
        }

        public void setData(java.util.List<T> data) {
            this.data = data;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}
