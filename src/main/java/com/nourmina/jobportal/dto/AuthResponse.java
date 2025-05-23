package com.nourmina.jobportal.dto;
// DTOs: Data Transfer Object -> classes used to send/ receive data from
// we can safetly filter data from the database we dont want it to be accessible like accessing the whole user including its password

// this is the response sent to the client after successfully logging in backend to frontend
// contains the token & indentity info
public class AuthResponse {

    private String token; // JWT authentication token
    private String role; // "CANDIDATE" or "RECRUITER"
    private String id; // id from the db
    private String email;

    // constructors
    public AuthResponse() {}

    public AuthResponse(String token, String role, String id, String email) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.email = email;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


}