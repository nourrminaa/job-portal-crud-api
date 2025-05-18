package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.JobPosting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    private final DataCache dataCache;

    public JobController(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @GetMapping
    public ResponseEntity<ArrayList<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(dataCache.getJobs());
    }

    @PostMapping
    public ResponseEntity<JobPosting> createJob(@RequestBody JobPosting job) {
        ArrayList<JobPosting> jobs = dataCache.getJobs();
        jobs.add(job);
        dataCache.setJobs(jobs);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        ArrayList<JobPosting> jobs = dataCache.getJobs();
        jobs.removeIf(job -> job.getId().equals(id));
        dataCache.setJobs(jobs);
        return ResponseEntity.ok().build();
    }
}