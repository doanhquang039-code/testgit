package com.example.hr.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Integer id) {
        super(resource + " không tìm thấy với ID: " + id);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " không tìm thấy với ID: " + id);
    }

    public ResourceNotFoundException(String resource, String field, String value) {
        super(resource + " không tìm thấy với " + field + ": " + value);
    }
}
