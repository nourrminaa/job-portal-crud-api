package com.nourmina.jobportal.dto;

// DTO used for when the user demands its info or another user's EXCLUDING sensitive info
public class UserResponseDTO {

    private String id;
    private String email;
    private String role;

    private String company;  // For recruiters
    private String skills;   // For candidates
    private String resume;   // For candidates

    // Constructors, getters, and setters
    public UserResponseDTO() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
}