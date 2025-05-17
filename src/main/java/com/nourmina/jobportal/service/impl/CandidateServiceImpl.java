package com.nourmina.jobportal.service.impl;

// Importing model classes (data objects)
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.JobPosting;

// Importing repository interfaces (for database access)
import com.nourmina.jobportal.repository.CandidateRepository;
import com.nourmina.jobportal.repository.JobPostingRepository;
import com.nourmina.jobportal.repository.ApplicationRepository;

// Importing service interface and custom exception
import com.nourmina.jobportal.service.CandidateService;
import com.nourmina.jobportal.exception.ResourceNotFoundException;

// Spring annotations for dependency injection and service registration
import com.nourmina.jobportal.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Tells Spring this class provides business logic and should be managed as a service
public class CandidateServiceImpl implements CandidateService {

    // Repositories needed to interact with the database
    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;

    @Autowired // Spring will automatically inject the required repositories through the constructor
    public CandidateServiceImpl(CandidateRepository candidateRepository,
                                JobPostingRepository jobPostingRepository,
                                ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Candidate registerCandidate(Candidate candidate) {
        // Save the candidate to the database (you can also validate fields or encode password here)
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate getCandidateById(String id) {
        // Try to find the candidate by ID; if not found, throw a custom exception
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));
    }

    @Override
    public Candidate updateProfile(String id, Candidate updatedCandidate) {
        // Get the existing candidate by ID
        Candidate existing = getCandidateById(id);

        // Update fields of the existing candidate (currently only the name; more can be added)
        existing.setFirstName(updatedCandidate.getFirstName());
        existing.setLastName(updatedCandidate.getLastName());

        // Save the updated candidate back to the database
        return candidateRepository.save(existing);
    }

    @Override
    public Application applyToJob(String candidateId, String jobId) {
        // Find the candidate by ID (or throw an error if not found)
        Candidate candidate = getCandidateById(candidateId);

        // Find the job by ID (or throw an error if not found)
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Create a new job application and link it to the candidate and job
        Application app = new Application();
        app.setCandidateId(candidate.getId());
        app.setJobPostingId(jobPosting.getId());
        app.setStatus("PENDING"); // Set the initial status to PENDING

        // Save the application in the database
        return applicationRepository.save(app);
    }
}
