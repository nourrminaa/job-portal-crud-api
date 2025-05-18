package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Application;

public class ApplicationValidator {

    public static void validate(Application application) {
        if (application == null) {
            throw new BadRequestException("Application cannot be null.");
        }
        if (application.getCandidateId() == null || application.getCandidateId().trim().isEmpty()) {
            throw new BadRequestException("Application must have a candidate ID.");
        }
        if (application.getJobPostingId() == null || application.getJobPostingId().trim().isEmpty()) {
            throw new BadRequestException("Application must have a job ID.");
        }
        if (application.getStatus() == null || application.getStatus().trim().isEmpty()) {
            throw new BadRequestException("Application status is required.");
        }
    }
}
