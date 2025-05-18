package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.JobPosting;

import java.util.ArrayList;
import java.util.Optional;

public interface JobPostingService {
    JobPosting createJob(JobPosting job);
    ArrayList<JobPosting> getAllJobs();
    ArrayList<JobPosting> getJobsByRecruiterId(String recruiterId);
    JobPosting updateJob(JobPosting job);
    void deleteJob(String jobId);
    Optional<JobPosting> findById(String jobId);
}
