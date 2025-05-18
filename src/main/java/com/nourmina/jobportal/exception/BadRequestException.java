package com.nourmina.jobportal.exception;

// This exception is thrown when a request to the server is invalid or cannot be processed.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

