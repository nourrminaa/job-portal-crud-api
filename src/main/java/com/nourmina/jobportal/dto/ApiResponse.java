package com.nourmina.jobportal.dto;

import java.time.LocalDateTime;

/**
 * Generic API response class for standardized responses
 */
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    // Constructors
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}