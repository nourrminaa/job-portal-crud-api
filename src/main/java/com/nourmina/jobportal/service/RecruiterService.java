package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Recruiter;

import java.util.Optional;
import java.util.ArrayList;

public interface RecruiterService {
    Recruiter registerRecruiter(Recruiter recruiter);
    Optional<Recruiter> findById(String id);
    JobPosting createJob(String recruiterId, JobPosting jobPosting);
    JobPosting updateJob(String jobId, JobPosting updatedJob);
    void deleteJob(String jobId);
    ArrayList<Recruiter> filterRecruitersByKeyword(String keyword);
}
