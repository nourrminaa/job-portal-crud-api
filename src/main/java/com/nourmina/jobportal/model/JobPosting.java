package com.nourmina.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Document  // Marks this class as a MongoDB document
public class JobPosting {
    @Id
    private String id;
    private String title;
    private String description;
    private String location;
    private ArrayList<String> requiredSkills;
    private String recruiterId;
    private Double salary;
    private String company;
    private LocalDateTime postDate;
    private String status; // ✅ Added missing field

    // Constructors
    public JobPosting() {
        this.requiredSkills = new ArrayList<>();
    }

    public JobPosting(String title, String description, String location, ArrayList<String> requiredSkills, String recruiterID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.requiredSkills = requiredSkills;
        this.recruiterId = recruiterID;
        this.postDate = LocalDateTime.now(); // Optional: set posting date by default
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public ArrayList<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(ArrayList<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getRecruiterId() { return recruiterId; }
    public void setRecruiterId(String recruiterId){ this.recruiterId = recruiterId; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public LocalDateTime getPostDate() { return postDate; }
    public void setPostDate(LocalDateTime postDate) { this.postDate = postDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
