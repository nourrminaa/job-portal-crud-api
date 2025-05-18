package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.RecruiterDTO;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.security.CurrentUser;
import com.nourmina.jobportal.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        List<Recruiter> recruiters = recruiterService.getAllRecruiters();
        return ResponseEntity.ok(recruiters);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Recruiter> getRecruiterById(@PathVariable String id) {
        Recruiter recruiter = recruiterService.getRecruiterById(id);
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Recruiter> getCurrentRecruiterProfile(@CurrentUser UserDetails currentUser) {
        Recruiter recruiter = recruiterService.getRecruiterByEmail(currentUser.getUsername());
        return ResponseEntity.ok(recruiter);
    }

    @GetMapping("/company/{company}")
    public ResponseEntity<List<Recruiter>> getRecruitersByCompany(@PathVariable String company) {
        List<Recruiter> recruiters = recruiterService.getRecruitersByCompany(company);
        return ResponseEntity.ok(recruiters);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id)")
    public ResponseEntity<ApiResponse> updateRecruiter(@PathVariable String id,
                                                       @Valid @RequestBody RecruiterDTO recruiterDTO) {
        Recruiter updatedRecruiter = recruiterService.updateRecruiter(id, recruiterDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Recruiter updated successfully", updatedRecruiter));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentUser(#id) or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteRecruiter(@PathVariable String id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.ok(new ApiResponse(true, "Recruiter deleted successfully"));
    }
}