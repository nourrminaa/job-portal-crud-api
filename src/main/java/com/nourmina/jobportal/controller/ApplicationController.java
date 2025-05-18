package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.ApplicationDTO;
import com.nourmina.jobportal.dto.ApplicationResponseDTO;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.security.CurrentUser;
import com.nourmina.jobportal.service.ApplicationService;
import com.nourmina.jobportal.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Page<Application>> getAllApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Application> applications = applicationService.getAllApplicationsWithPagination(pageable);
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
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplicationsByCandidate(
            @PathVariable String candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponseDTO> applications = applicationService.getApplicationsByCandidateWithPagination(candidateId, pageable);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/job-posting/{jobPostingId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isJobPostingRecruiter(#jobPostingId)")
    public ResponseEntity<Page<ApplicationResponseDTO>> getApplicationsByJobPosting(
            @PathVariable String jobPostingId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponseDTO> applications = applicationService.getApplicationsByJobPostingWithPagination(jobPostingId, pageable);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Page<ApplicationResponseDTO>> getCurrentUserApplications(
            @CurrentUser UserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        String candidateId = securityService.getCurrentUserId(currentUser);
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ApplicationResponseDTO> applications = applicationService.getApplicationsByCandidateWithPagination(candidateId, pageable);
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