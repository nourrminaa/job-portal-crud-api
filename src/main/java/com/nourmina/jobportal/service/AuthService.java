package com.nourmina.jobportal.service;

import com.nourmina.jobportal.dto.AuthRequest;
import com.nourmina.jobportal.dto.AuthResponse;
import com.nourmina.jobportal.dto.CandidateDTO;
import com.nourmina.jobportal.dto.RecruiterDTO;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.repository.UserRepository;
import com.nourmina.jobportal.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private RecruiterService recruiterService;

    public AuthResponse login(AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);

        return new AuthResponse(
                jwt,
                user.getRole(),
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public User registerCandidate(CandidateDTO candidateDTO) {
        return candidateService.createCandidate(candidateDTO);
    }

    public User registerRecruiter(RecruiterDTO recruiterDTO) {
        return recruiterService.createRecruiter(recruiterDTO);
    }
}