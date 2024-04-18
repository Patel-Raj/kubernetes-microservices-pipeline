package com.cloud.calculator.exceptions;

public class FileNameNotPresentException  extends RuntimeException {
    public FileNameNotPresentException(String message) {
        super(message);
    }
}
