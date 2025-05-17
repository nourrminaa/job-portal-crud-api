package com.nourmina.jobportal.service.impl;

// Importing model classes (data structures used in the app)
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Recruiter;

// Importing repository interfaces (used to talk to the database)
import com.nourmina.jobportal.repository.JobPostingRepository;
import com.nourmina.jobportal.repository.RecruiterRepository;

// Importing service interface and custom error class
import com.nourmina.jobportal.service.RecruiterService;
import com.nourmina.jobportal.exception.ResourceNotFoundException;

// Importing Spring annotations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Tells Spring this class provides business logic and should be managed as a service component
public class RecruiterServiceImpl implements RecruiterService {

    // These are the tools (repositories) needed to do database operations
    private final RecruiterRepository recruiterRepository;
    private final JobPostingRepository jobPostingRepository;

    // Spring will automatically inject (provide) these repositories via constructor
    @Autowired
    public RecruiterServiceImpl(RecruiterRepository recruiterRepository, JobPostingRepository jobPostingRepository) {
        this.recruiterRepository = recruiterRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    @Override
    public Recruiter registerRecruiter(Recruiter recruiter) {
        // Save the recruiter in the database
        return recruiterRepository.save(recruiter);
    }

    @Override
    public JobPosting createJob(String recruiterId, JobPosting jobPosting) {
        // Find the recruiter by ID; throw an error if not found
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        // Link the job to the recruiter who created it
        jobPosting.setRecruiterId(recruiter.getId());

        // Save the new job in the database
        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public JobPosting updateJob(String jobId, JobPosting updatedJob) {
        // Find the existing job by ID; throw error if not found
        JobPosting existing = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Update the job's title (you can update other fields too, like description, salary, etc.)
        existing.setTitle(updatedJob.getTitle());

        // Save the updated job in the database
        return jobPostingRepository.save(existing);
    }

    @Override
    public void deleteJob(String jobId) {
        // Check if the job exists before deleting it
        if (!jobPostingRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job not found");
        }

        // If it exists, delete it
        jobPostingRepository.deleteById(jobId);
    }
}
