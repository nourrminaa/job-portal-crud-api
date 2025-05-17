package com.nourmina.jobportal.service.impl;

// Importing the Job model class (represents a job posting)
import com.nourmina.jobportal.model.JobPosting;

// Importing the repository to fetch/store Job data
import com.nourmina.jobportal.repository.JobPostingRepository;

// Importing the service interface that defines our contract
import com.nourmina.jobportal.service.JobPostingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service  // Marks this class as a Spring service component
public class JobPostingServiceImpl implements JobPostingService {

    // Repository used for database operations on Job entities
    private final JobPostingRepository jobPostingRepository;

    @Autowired // Spring will inject the JobRepository implementation here
    public JobPostingServiceImpl(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public ArrayList<JobPosting> listJobs() {
        // Retrieve all jobs from the database
        // findAll() returns a List<Job>, so we wrap it in an ArrayList
        return new ArrayList<JobPosting>(jobPostingRepository.findAll());
    }

    @Override
    public ArrayList<JobPosting> searchJobs(String keyword) {
        // Search for jobs whose title contains the keyword (case-insensitive)
        // findByTitleContainingIgnoreCase handles the query, then we wrap result in ArrayList
        return new ArrayList<JobPosting>(jobPostingRepository.findByTitleContainingIgnoreCase(keyword));
    }
}
