package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<ArrayList<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ArrayList<Application>> getApplicationsByCandidate(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.findApplicationsByCandidateId(candidateId));
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable String applicationId,
            @RequestParam String status
    ) {
        applicationService.updateApplicationStatus(applicationId, status);
        return ResponseEntity.noContent().build();
    }
}
