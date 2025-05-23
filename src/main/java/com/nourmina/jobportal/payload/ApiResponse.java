package com.nourmina.jobportal.payload;

import java.util.Map;
// basically also a DTO but is puut here for clarity

// used for API success and error messages
public class ApiResponse {
    private boolean success; // true -> success, false -> error
    private String message; // error or success message
    private Map<String, String> details; // error details

    // constructors
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(boolean success, String message, Map<String, String> details) {
        this.success = success;
        this.message = message;
        this.details = details;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details; }
}
