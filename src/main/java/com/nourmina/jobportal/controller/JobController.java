package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.service.MongoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    private final DataCache dataCache;
    private final MongoDBService mongoDBService;

    public JobController(DataCache dataCache, MongoDBService mongoDBService) {
        this.dataCache = dataCache;
        this.mongoDBService = mongoDBService;
    }

    // Get all jobs from cache
    @GetMapping
    public ResponseEntity<ArrayList<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(dataCache.getJobs());
    }

    // Create new job and update cache
    @PostMapping
    public ResponseEntity<JobPosting> createJob(@RequestBody JobPosting job) {
        ArrayList<JobPosting> jobs = dataCache.getJobs();
        jobs.add(job);
        dataCache.setJobs(jobs);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok(job);
    }

    // Delete all jobs and rewrite cache
    @DeleteMapping
    public ResponseEntity<Void> deleteAndRewriteJobs(@RequestBody ArrayList<JobPosting> newJobs) {
        dataCache.setJobs(newJobs);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok().build();
    }

    // Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        return dataCache.getJobs().stream()
                .filter(job -> job.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update job
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable String id, @RequestBody JobPosting updatedJob) {
        ArrayList<JobPosting> jobs = dataCache.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getId().equals(id)) {
                jobs.set(i, updatedJob);
                dataCache.setJobs(jobs);

                // On demand sync with MongoDB
                mongoDBService.saveDataToMongoDB();

                return ResponseEntity.ok(updatedJob);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Delete job by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        ArrayList<JobPosting> jobs = dataCache.getJobs();
        boolean removed = jobs.removeIf(job -> job.getId().equals(id));

        if (removed) {
            dataCache.setJobs(jobs);

            // On demand sync with MongoDB
            mongoDBService.saveDataToMongoDB();

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get jobs by recruiter ID
    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<ArrayList<JobPosting>> getJobsByRecruiterId(@PathVariable String recruiterId) {
        ArrayList<JobPosting> recruiterJobs = new ArrayList<>();

        for (JobPosting job : dataCache.getJobs()) {
            if (job.getRecruiterId().equals(recruiterId)) {
                recruiterJobs.add(job);
            }
        }

        return ResponseEntity.ok(recruiterJobs);
    }
}