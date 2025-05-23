package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    // Submit a job application (candidate only)
    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Application> applyToJob(
            @RequestBody Map<String, String> applicationRequest,
            Authentication auth) {

        String jobId = applicationRequest.get("jobId");
        String coverLetter = applicationRequest.get("coverLetter");

        if (jobId == null || jobId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job ID is required");
        }

        try {
            String userId = auth.getName();
            Application application = applicationService.applyToJob(jobId, userId, coverLetter);
            return ResponseEntity.status(HttpStatus.CREATED).body(application);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get applications for a specific job (recruiter only)
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<Application>> getApplicationsForJob(
            @PathVariable String jobId,
            Authentication auth) {

        try {
            String userId = auth.getName();
            List<Application> applications = applicationService.getApplicationsForJob(jobId, userId);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get applications submitted by a candidate (for the candidate's profile)
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<Application>> getUserApplications(Authentication auth) {
        String userId = auth.getName();
        List<Application> applications = applicationService.getApplicationsForUser(userId);
        return ResponseEntity.ok(applications);
    }

    // Get a specific application
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable String id, Authentication auth) {
        try {
            Application application = applicationService.getApplication(id);
            String userId = auth.getName();

            // Security check - only the candidate who applied or the recruiter who posted the job can view
            // This would be better implemented in the service layer

            return ResponseEntity.ok(application);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Update application status (recruiter only)
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> statusUpdate,
            Authentication auth) {

        String status = statusUpdate.get("status");

        if (status == null || status.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
        }

        try {
            String userId = auth.getName();
            Application updatedApplication = applicationService.updateApplicationStatus(id, status, userId);
            return ResponseEntity.ok(updatedApplication);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Withdraw an application (candidate only)
    @PutMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Void> withdrawApplication(@PathVariable String id, Authentication auth) {
        try {
            String userId = auth.getName();
            applicationService.withdrawApplication(id, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Check if candidate has already applied to a job
    @GetMapping("/check/{jobId}")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Map<String, Boolean>> hasApplied(@PathVariable String jobId, Authentication auth) {
        String userId = auth.getName();
        boolean hasApplied = applicationService.hasApplied(jobId, userId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("applied", hasApplied);

        return ResponseEntity.ok(response);
    }
}