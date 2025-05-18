package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Recruiter;

public class RecruiterValidator {

    public static void validate(Recruiter recruiter) {
        if (recruiter == null) {
            throw new BadRequestException("Recruiter cannot be null.");
        }
        if (recruiter.getEmail() == null || !recruiter.getEmail().contains("@")) {
            throw new BadRequestException("Invalid or missing recruiter email.");
        }
        if (recruiter.getPassword() == null || recruiter.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters long.");
        }
        if (recruiter.getCompany() == null || recruiter.getCompany().trim().isEmpty()) {
            throw new BadRequestException("Recruiter must be associated with a company.");
        }
    }
}
