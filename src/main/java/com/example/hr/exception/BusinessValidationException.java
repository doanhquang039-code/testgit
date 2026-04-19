package com.example.hr.exception;

public class BusinessValidationException extends RuntimeException {
    private final String fieldName;

    public BusinessValidationException(String message) {
        super(message);
        this.fieldName = null;
    }

    public BusinessValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
