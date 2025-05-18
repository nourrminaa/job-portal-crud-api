// Path: src/main/java/com/nourmina/jobportal/controller/JobPostingController.java (Updated with Validator)
package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.JobPostingDTO;
import com.nourmina.jobportal.dto.SearchCriteriaDTO;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.security.CurrentUser;
import com.nourmina.jobportal.service.JobPostingService;
import com.nourmina.jobportal.service.SecurityService;
import com.nourmina.jobportal.validation.SearchCriteriaValidator;
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
@RequestMapping("/api/job-postings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SearchCriteriaValidator searchCriteriaValidator;

    @GetMapping
    public ResponseEntity<Page<JobPosting>> getAllJobPostings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<JobPosting> jobPostings = jobPostingService.getAllJobPostings(pageable);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable String id) {
        JobPosting jobPosting = jobPostingService.getJobPostingById(id);
        return ResponseEntity.ok(jobPosting);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPosting>> searchJobPostings(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<JobPosting> jobPostings = jobPostingService.searchJobPostings(keyword, pageable);
        return ResponseEntity.ok(jobPostings);
    }

    @PostMapping("/advanced-search")
    public ResponseEntity<Page<JobPosting>> advancedSearch(
            @RequestBody SearchCriteriaDTO searchCriteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "postDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        // Validate search criteria
        searchCriteriaValidator.validate(searchCriteria);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<JobPosting> results = jobPostingService.advancedSearch(searchCriteria, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<JobPosting>> getJobPostingsByLocation(@PathVariable String location) {
        List<JobPosting> jobPostings = jobPostingService.searchJobPostingsByLocation(location);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/skills/{skill}")
    public ResponseEntity<List<JobPosting>> getJobPostingsBySkill(@PathVariable String skill) {
        List<JobPosting> jobPostings = jobPostingService.searchJobPostingsBySkill(skill);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<JobPosting>> getJobPostingsByRecruiter(@PathVariable String recruiterId) {
        List<JobPosting> jobPostings = jobPostingService.getJobPostingsByRecruiter(recruiterId);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<JobPosting>> getJobPostingsBySalaryRange(
            @RequestParam Double minSalary,
            @RequestParam(required = false) Double maxSalary) {

        // Validate salary range
        if (maxSalary != null && minSalary > maxSalary) {
            return ResponseEntity.badRequest().build();
        }

        List<JobPosting> jobPostings = jobPostingService.searchJobPostingsBySalaryRange(minSalary, maxSalary);
        return ResponseEntity.ok(jobPostings);
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse> createJobPosting(
            @Valid @RequestBody JobPostingDTO jobPostingDTO,
            @CurrentUser UserDetails currentUser) {

        // Get recruiter ID from authenticated user
        String recruiterId = jobPostingService.getRecruiterIdFromEmail(currentUser.getUsername());

        JobPosting createdJobPosting = jobPostingService.createJobPosting(jobPostingDTO, recruiterId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Job posting created successfully", createdJobPosting));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') and @securityService.isJobPostingOwner(#id)")
    public ResponseEntity<ApiResponse> updateJobPosting(
            @PathVariable String id,
            @Valid @RequestBody JobPostingDTO jobPostingDTO) {

        JobPosting updatedJobPosting = jobPostingService.updateJobPosting(id, jobPostingDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Job posting updated successfully", updatedJobPosting));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') and @securityService.isJobPostingOwner(#id)")
    public ResponseEntity<ApiResponse> deleteJobPosting(@PathVariable String id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.ok(new ApiResponse(true, "Job posting deleted successfully"));
    }
}