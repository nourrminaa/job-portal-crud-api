package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.model.JobPosting;

public interface RecruiterService {
    Recruiter registerRecruiter(Recruiter recruiter);
    JobPosting createJob(String recruiterId, JobPosting jobPosting);
    JobPosting updateJob(String jobId, JobPosting updatedJob);
    void deleteJob(String jobId);
}
