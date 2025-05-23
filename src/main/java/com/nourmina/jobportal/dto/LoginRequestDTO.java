package com.nourmina.jobportal.dto;

import jakarta.validation.constraints.NotBlank;

// DTO for login request from the user interface
// takes the user credentials
public class LoginRequestDTO {

    @NotBlank(message = "Email is required") // validation
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // constructors
    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}