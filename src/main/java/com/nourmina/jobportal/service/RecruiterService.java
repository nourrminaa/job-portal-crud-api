package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.model.JobPosting;

import java.util.ArrayList;
import java.util.Optional;

public interface RecruiterService {
    Recruiter save(Recruiter recruiter);
    Optional<Recruiter> findById(String id);
    Recruiter updateRecruiter(Recruiter recruiter);
    void deleteRecruiter(String id);
    ArrayList<Recruiter> getAllRecruiters();
    ArrayList<JobPosting> getAllRecruiterJobs(String recruiterId);
    JobPosting postJob(JobPosting job, String recruiterId);
}
