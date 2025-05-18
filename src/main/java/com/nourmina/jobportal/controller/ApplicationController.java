package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.Application;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final DataCache dataCache;

    public ApplicationController(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ArrayList<Application>> getApplicationsByCandidate(@PathVariable String candidateId) {
        ArrayList<Application> applications = dataCache.getApplications().stream()
                .filter(app -> app.getCandidateId().equals(candidateId))
                .collect(Collectors.toCollection(ArrayList::new));
        return ResponseEntity.ok(applications);
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        ArrayList<Application> applications = dataCache.getApplications();
        applications.add(application);
        dataCache.setApplications(applications);
        return ResponseEntity.ok(application);
    }
}