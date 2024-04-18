package com.cloud.validator.exceptions;

public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}
