package com.nourmina.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Document(collection = "applications")
public class Application {

    @Id
    private String id;

    // job applying to
    @NotNull(message = "Job ID is required")
    private String jobId;

    // candidate id
    @NotNull(message = "User ID is required")
    private String userId;

    private String coverLetter;

    private String status; // PENDING, REVIEWING, ACCEPTED, REJECTED, WITHDRAWN

    private LocalDateTime appliedAt;

    public Application() {
        this.appliedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Application(String jobId, String userId, String coverLetter) {
        this.jobId = jobId;
        this.userId = userId;
        this.coverLetter = coverLetter;
        this.appliedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", jobId='" + jobId + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                ", appliedAt=" + appliedAt +
                '}';
    }
}