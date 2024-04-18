package com.cloud.calculator.exceptions;

public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}
