package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.validation.CandidateValidator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final DataCache dataCache;
    private final JobPostingService jobPostingService;
    private final ApplicationService applicationService;

    public CandidateServiceImpl(DataCache dataCache,
                                JobPostingService jobPostingService,
                                ApplicationService applicationService) {
        this.dataCache = dataCache;
        this.jobPostingService = jobPostingService;
        this.applicationService = applicationService;
    }

    // Helper to get only Candidates from users
    private ArrayList<Candidate> getAllCandidates() {
        ArrayList<Candidate> candidates = new ArrayList<>();
        for (User user : dataCache.getUsers()) {
            if (user instanceof Candidate) {
                candidates.add((Candidate) user);
            }
        }
        return candidates;
    }

    @Override
    public Candidate save(Candidate candidate) {
        CandidateValidator.validate(candidate);

        ArrayList<Candidate> candidates = getAllCandidates();
        for (Candidate c : candidates) {
            if (c.getEmail().equalsIgnoreCase(candidate.getEmail())) {
                throw new BadRequestException("Candidate with this email already exists.");
            }
        }

        ArrayList<User> currentUsers = dataCache.getUsers();
        currentUsers.add(candidate);
        dataCache.setUsers(currentUsers);
        return candidate;
    }

    @Override
    public void applyToJob(String candidateId, String jobId) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new BadRequestException("Candidate ID is required.");
        }
        if (jobId == null || jobId.isBlank()) {
            throw new BadRequestException("Job ID is required.");
        }

        Optional<Candidate> candidateOpt = findById(candidateId);
        if (candidateOpt.isEmpty()) {
            throw new BadRequestException("Candidate not found.");
        }

        Optional<JobPosting> jobOpt = jobPostingService.findById(jobId);
        if (jobOpt.isEmpty()) {
            throw new BadRequestException("Job not found.");
        }

        Application application = new Application();
        application.setCandidateId(candidateId);
        application.setId(jobId); // assuming you're reusing id field as jobId

        applicationService.save(application);
    }

    @Override
    public ArrayList<JobPosting> viewAllAvailableJobs() {
        return jobPostingService.getAllJobs();
    }

    @Override
    public ArrayList<JobPosting> viewAppliedJobs(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new BadRequestException("Candidate ID is required.");
        }

        ArrayList<Application> applications = applicationService.getApplicationsByCandidateId(candidateId);
        ArrayList<JobPosting> appliedJobs = new ArrayList<>();

        for (Application app : applications) {
            Optional<JobPosting> jobOpt = jobPostingService.findById(app.getId());
            jobOpt.ifPresent(appliedJobs::add);
        }
        return appliedJobs;
    }

    @Override
    public Candidate updateProfile(Candidate candidate) {
        CandidateValidator.validate(candidate);

        Optional<Candidate> existingOpt = findById(candidate.getId());
        if (existingOpt.isEmpty()) {
            throw new BadRequestException("Candidate with id " + candidate.getId() + " not found.");
        }

        ArrayList<User> currentUsers = dataCache.getUsers();
        for (int i = 0; i < currentUsers.size(); i++) {
            User user = currentUsers.get(i);
            if (user instanceof Candidate && candidate.getId().equals(user.getId())) {
                currentUsers.set(i, candidate);
                dataCache.setUsers(currentUsers);
                return candidate;
            }
        }

        return candidate;
    }

    @Override
    public void deleteAccount(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new BadRequestException("Candidate ID cannot be empty");
        }

        ArrayList<User> currentUsers = dataCache.getUsers();
        boolean removed = false;

        for (int i = 0; i < currentUsers.size(); i++) {
            User user = currentUsers.get(i);
            if (user instanceof Candidate && candidateId.equals(user.getId())) {
                currentUsers.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new BadRequestException("Candidate with id " + candidateId + " not found.");
        }

        dataCache.setUsers(currentUsers);
    }

    @Override
    public Optional<Candidate> findById(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            return Optional.empty();
        }

        ArrayList<Candidate> candidates = getAllCandidates();
        for (Candidate c : candidates) {
            if (candidateId.equals(c.getId())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}