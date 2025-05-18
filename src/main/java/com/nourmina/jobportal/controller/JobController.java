package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.service.JobPostingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobPostingService jobPostingService;

    public JobController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping
    public ResponseEntity<ArrayList<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(jobPostingService.listJobs());
    }

    @GetMapping("/search")
    public ResponseEntity<ArrayList<JobPosting>> searchJobs(@RequestParam String keyword) {
        return ResponseEntity.ok(jobPostingService.searchJobs(keyword));
    }

    @GetMapping("/sorted")
    public ResponseEntity<ArrayList<JobPosting>> getJobsSortedBySalary() {
        return ResponseEntity.ok(jobPostingService.sortJobsBySalary());
    }

    @GetMapping("/page")
    public ResponseEntity<ArrayList<JobPosting>> getJobsPaginated(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(jobPostingService.paginateJobs(page, size));
    }
}
