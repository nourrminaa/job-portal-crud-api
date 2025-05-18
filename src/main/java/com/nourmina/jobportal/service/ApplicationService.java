package com.example.jobportal.service;

import com.example.jobportal.model.Application;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // Apply to a job
    public Application applyToJob(String jobId, String userId, String coverLetter) {
        // Verify user is a candidate
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"CANDIDATE".equals(user.getRole())) {
            throw new IllegalArgumentException("Only candidates can apply to jobs");
        }

        // Check if job exists and is open
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!"OPEN".equals(job.getStatus())) {
            throw new IllegalStateException("This job is not open for applications");
        }

        // Check if already applied
        if (applicationRepository.findByJobIdAndUserId(jobId, userId).isPresent()) {
            throw new IllegalStateException("You have already applied to this job");
        }

        // Create and save the application
        Application application = new Application(jobId, userId, coverLetter);
        return applicationRepository.save(application);
    }

    // Get applications for a job (for recruiters)
    public List<Application> getApplicationsForJob(String jobId, String userId) {
        // Check if the job belongs to this recruiter
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to view these applications");
        }

        return applicationRepository.findByJobId(jobId);
    }

    // Get applications by a candidate
    public List<Application> getApplicationsForUser(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByUserId(userId);
    }

    // Get a single application
    public Application getApplication(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    // Update application status (for recruiters)
    public Application updateApplicationStatus(String id, String status, String userId) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Check if the job belongs to this recruiter
        Job job = jobRepository.findById(application.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this application");
        }

        // Update status
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    // Withdraw application (for candidates)
    public void withdrawApplication(String id, String userId) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to withdraw this application");
        }

        application.setStatus("WITHDRAWN");
        applicationRepository.save(application);
    }

    // Check if candidate has already applied to a job
    public boolean hasApplied(String jobId, String userId) {
        return applicationRepository.findByJobIdAndUserId(jobId, userId).isPresent();
    }
}