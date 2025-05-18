package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.ApplicationDTO;
import com.nourmina.jobportal.dto.ApplicationResponseDTO;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.security.CurrentUser;
import com.nourmina.jobportal.service.ApplicationService;
import com.nourmina.jobportal.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SecurityService securityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isApplicationOwner(#id) or @securityService.isJobPostingRecruiter(#id)")
    public ResponseEntity<Application> getApplicationById(@PathVariable String id) {
        Application application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/candidate/{candidateId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#candidateId)")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByCandidate(@PathVariable String candidateId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsByCandidate(candidateId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job-posting/{jobPostingId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobPostingRecruiter(#jobPostingId)")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByJobPosting(@PathVariable String jobPostingId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsByJobPosting(jobPostingId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponseDTO>> getCurrentUserApplications(@CurrentUser UserDetails currentUser) {
        String candidateId = securityService.getCurrentUserId(currentUser);
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsByCandidate(candidateId);
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApiResponse> applyForJob(
            @Valid @RequestBody ApplicationDTO applicationDTO,
            @CurrentUser UserDetails currentUser) {

        // Ensure the candidate ID in the DTO matches the current user
        String candidateId = securityService.getCurrentUserId(currentUser);
        applicationDTO.setCandidateId(candidateId);

        Application application = applicationService.apply(applicationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Application submitted successfully", application));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER') and @securityService.isJobPostingRecruiter(#id)")
    public ResponseEntity<ApiResponse> updateApplicationStatus(
            @PathVariable String id,
            @RequestParam String status) {

        Application updatedApplication = applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok(
                new ApiResponse(true, "Application status updated successfully", updatedApplication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isApplicationOwner(#id)")
    public ResponseEntity<ApiResponse> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.ok(new ApiResponse(true, "Application deleted successfully"));
    }
}