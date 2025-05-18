package com.nourmina.jobportal.service.impl;

import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.service.CandidateService;
import com.nourmina.jobportal.validation.CandidateValidator;
import com.nourmina.jobportal.validation.JobPostingValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final ArrayList<Candidate> candidates = new ArrayList<>();
    private final ArrayList<JobPosting> jobPostings;
    private final ArrayList<Application> applications;

    public CandidateServiceImpl(ArrayList<JobPosting> jobPostings, ArrayList<Application> applications) {
        this.jobPostings = jobPostings;
        this.applications = applications;
    }

    @Override
    public Candidate registerCandidate(Candidate candidate) {
        CandidateValidator.validate(candidate);

        boolean emailExists = candidates.stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(candidate.getEmail()));
        if (emailExists) {
            throw new BadRequestException("Candidate email already registered");
        }
        candidates.add(candidate);
        return candidate;
    }

    @Override
    public Optional<Candidate> findById(String id) {
        return candidates.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public Candidate updateProfile(String id, Candidate updatedCandidate) {
        CandidateValidator.validate(updatedCandidate);

        Candidate existing = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        existing.setFirstName(updatedCandidate.getFirstName());
        existing.setLastName(updatedCandidate.getLastName());
        existing.setResume(updatedCandidate.getResume());
        existing.setSkills(updatedCandidate.getSkills());
        return existing;
    }

    @Override
    public Application applyToJob(String candidateId, String jobId) {
        Candidate candidate = findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        JobPosting jobPosting = jobPostings.stream()
                .filter(j -> j.getId().equals(jobId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        // Optional: You could validate jobPosting here if needed
        JobPostingValidator.validate(jobPosting);

        Application app = new Application();
        app.setCandidateId(candidate.getId());
        app.setJobPostingId(jobPosting.getId());
        app.setStatus("PENDING");
        applications.add(app);
        return app;
    }

    public ArrayList<Candidate> getAllCandidates() {
        return candidates;
    }
}
