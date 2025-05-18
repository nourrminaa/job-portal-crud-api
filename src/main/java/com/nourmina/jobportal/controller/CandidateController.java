package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.CandidateDTO;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.security.CurrentUser;
import com.nourmina.jobportal.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<Candidate> getCurrentCandidateProfile(@CurrentUser UserDetails currentUser) {
        Candidate candidate = candidateService.getCandidateByEmail(currentUser.getUsername());
        return ResponseEntity.ok(candidate);
    }

    @GetMapping("/skills/{skill}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<Candidate>> getCandidatesBySkill(@PathVariable String skill) {
        List<Candidate> candidates = candidateService.getCandidatesBySkill(skill);
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<ApiResponse> updateCandidate(@PathVariable String id,
                                                       @Valid @RequestBody CandidateDTO candidateDTO) {
        Candidate updatedCandidate = candidateService.updateCandidate(id, candidateDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Candidate updated successfully", updatedCandidate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCandidate(@PathVariable String id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok(new ApiResponse(true, "Candidate deleted successfully"));
    }
}