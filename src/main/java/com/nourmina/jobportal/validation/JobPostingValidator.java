package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.JobPosting;

public class JobPostingValidator {

    public static void validate(JobPosting job) {
        if (job == null) {
            throw new BadRequestException("Job cannot be null.");
        }
        if (job.getTitle() == null || job.getTitle().trim().isEmpty()) {
            throw new BadRequestException("Job title cannot be empty.");
        }
        if (job.getCompany() == null || job.getCompany().trim().isEmpty()) {
            throw new BadRequestException("Job must have a company name.");
        }
        if (job.getRequiredSkills() == null || job.getRequiredSkills().isEmpty()) {
            throw new BadRequestException("Job must have at least one required skill.");
        }
    }
}
