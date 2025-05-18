package com.nourmina.jobportal.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class JobPostingDTO {

    private String id;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Job location is required")
    private String location;

    private ArrayList<String> requiredSkills;

    private String recruiterId;

    @Positive(message = "Salary must be a positive number")
    private Double salary;

    @NotBlank(message = "Company name is required")
    private String company;

    private LocalDateTime postDate;

    private String status;

    // Constructors
    public JobPostingDTO() {
        this.requiredSkills = new ArrayList<>();
        this.postDate = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(ArrayList<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
