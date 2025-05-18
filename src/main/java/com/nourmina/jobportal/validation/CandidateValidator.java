package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Candidate;

public class CandidateValidator {

    public static void validate(Candidate candidate) {
        if (candidate == null) {
            throw new BadRequestException("Candidate cannot be null.");
        }
        if (candidate.getEmail() == null || !candidate.getEmail().contains("@")) {
            throw new BadRequestException("Invalid or missing candidate email.");
        }
        if (candidate.getPassword() == null || candidate.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters long.");
        }
        if (candidate.getSkills() == null || candidate.getSkills().isEmpty()) {
            throw new BadRequestException("Candidate must have at least one skill.");
        }
    }
}
