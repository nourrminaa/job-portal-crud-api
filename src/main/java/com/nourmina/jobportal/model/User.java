package com.nourmina.jobportal.model;

// to map the class to mongoDB
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// to validate the fields
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDateTime; // used for createdAt (user account)
                                //          postedAt (job)
                                //          appliedAt (application)
                                // format: y-m-dTh:m:s

@Document(collection = "users") // mark the class as a MongoDB "users" collection
public class User {
    @Id // to mark the field as the primary key
    private String id;

    @NotBlank(message = "Name is required") // to validate that the field cannot be empty
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String role; // "recruiter" or "candidate"
    private String company; // for recruiters
    private String skills; // for candidates
    private String resume; // URL to resume for candidates

    private LocalDateTime createdAt;

    // constructors
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}