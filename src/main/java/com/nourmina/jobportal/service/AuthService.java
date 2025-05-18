package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.util.IdGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    private final CandidateService candidateService;
    private final RecruiterService recruiterService;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    public AuthService(CandidateService candidateService,
                       RecruiterService recruiterService,
                       UserService userService) {
        this.candidateService = candidateService;
        this.recruiterService = recruiterService;
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.idGenerator = new IdGenerator();
    }

    public User login(String email, String password) {
        // Basic validation
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }
        if (!email.contains("@")) {
            throw new BadRequestException("Invalid email format");
        }

        // Find user and verify password
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Don't send password back
        user.setPassword(null);
        return user;
    }

    public User signup(String email, String password, String firstName, String lastName,
                       String userType, String company, ArrayList<String> skills) {

        // Basic validation for common fields
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new BadRequestException("Invalid email format");
        }
        if (password == null || password.length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new BadRequestException("First name is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new BadRequestException("Last name is required");
        }
        if (userType == null || (!userType.equals("CANDIDATE") && !userType.equals("RECRUITER"))) {
            throw new BadRequestException("User type must be either CANDIDATE or RECRUITER");
        }

        // Check if email already exists
        if (userService.findByEmail(email).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        // Create user based on type
        User user;
        if (userType.equals("CANDIDATE")) {
            if (skills == null || skills.isEmpty()) {
                throw new BadRequestException("Candidate must have at least one skill");
            }

            Candidate candidate = new Candidate();
            candidate.setId(idGenerator.generateId("CAN"));
            candidate.setEmail(email);
            candidate.setPassword(passwordEncoder.encode(password));
            candidate.setFirstName(firstName);
            candidate.setLastName(lastName);
            candidate.setRole("CANDIDATE");
            candidate.setSkills(skills);

            user = candidateService.save(candidate);

        } else {  // RECRUITER
            if (company == null || company.trim().isEmpty()) {
                throw new BadRequestException("Company name is required for recruiters");
            }

            Recruiter recruiter = new Recruiter();
            recruiter.setId(idGenerator.generateId("REC"));
            recruiter.setEmail(email);
            recruiter.setPassword(passwordEncoder.encode(password));
            recruiter.setFirstName(firstName);
            recruiter.setLastName(lastName);
            recruiter.setRole("RECRUITER");
            recruiter.setCompany(company);

            user = recruiterService.save(recruiter);
        }

        // Don't send password back
        user.setPassword(null);
        return user;
    }
}