package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.service.MongoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final DataCache dataCache;
    private final MongoDBService mongoDBService;

    public ApplicationController(DataCache dataCache, MongoDBService mongoDBService) {
        this.dataCache = dataCache;
        this.mongoDBService = mongoDBService;
    }

    // Get all applications from cache
    @GetMapping
    public ResponseEntity<ArrayList<Application>> getAllApplications() {
        return ResponseEntity.ok(dataCache.getApplications());
    }

    // Create new application and update cache
    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        ArrayList<Application> applications = dataCache.getApplications();
        applications.add(application);
        dataCache.setApplications(applications);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok(application);
    }

    // Delete all applications and rewrite cache
    @DeleteMapping
    public ResponseEntity<Void> deleteAndRewriteApplications(@RequestBody ArrayList<Application> newApplications) {
        dataCache.setApplications(newApplications);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok().build();
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable String id) {
        return dataCache.getApplications().stream()
                .filter(app -> app.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update application status
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable String id, @RequestBody Application updatedApplication) {
        ArrayList<Application> applications = dataCache.getApplications();
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getId().equals(id)) {
                applications.set(i, updatedApplication);
                dataCache.setApplications(applications);

                // On demand sync with MongoDB
                mongoDBService.saveDataToMongoDB();

                return ResponseEntity.ok(updatedApplication);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Delete application by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        ArrayList<Application> applications = dataCache.getApplications();
        boolean removed = applications.removeIf(app -> app.getId().equals(id));

        if (removed) {
            dataCache.setApplications(applications);

            // On demand sync with MongoDB
            mongoDBService.saveDataToMongoDB();

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get applications by candidate ID
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ArrayList<Application>> getApplicationsByCandidateId(@PathVariable String candidateId) {
        ArrayList<Application> candidateApplications = new ArrayList<>(dataCache.getApplications().stream()
                .filter(app -> app.getCandidateId().equals(candidateId))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(candidateApplications);
    }

    // Get applications by job ID
    @GetMapping("/job/{jobId}")
    public ResponseEntity<ArrayList<Application>> getApplicationsByJobId(@PathVariable String jobId) {
        ArrayList<Application> jobApplications = new ArrayList<>(dataCache.getApplications().stream()
                .filter(app -> app.getJobPostingId().equals(jobId))
                .collect(Collectors.toList()));

        return ResponseEntity.ok(jobApplications);
    }
}