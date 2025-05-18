package com.nourmina.jobportal.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ApplicationDTO {

    private String id;

    @NotBlank(message = "Candidate ID is required")
    private String candidateId;

    @NotBlank(message = "Job Posting ID is required")
    private String jobPostingId;

    private String status;
    private LocalDateTime appliedDate;

    // Constructors
    public ApplicationDTO() {
        this.appliedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(String jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}