package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.ApiResponse;
import com.nourmina.jobportal.dto.AuthRequest;
import com.nourmina.jobportal.dto.AuthResponse;
import com.nourmina.jobportal.dto.CandidateDTO;
import com.nourmina.jobportal.dto.RecruiterDTO;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<ApiResponse> registerCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
        User user = authService.registerCandidate(candidateDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Candidate registered successfully", user));
    }

    @PostMapping("/register/recruiter")
    public ResponseEntity<ApiResponse> registerRecruiter(@Valid @RequestBody RecruiterDTO recruiterDTO) {
        User user = authService.registerRecruiter(recruiterDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Recruiter registered successfully", user));
    }
}