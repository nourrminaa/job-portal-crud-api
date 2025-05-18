package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.validation.RecruiterValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final DataCache dataCache;
    private final JobPostingService jobPostingService;

    public RecruiterServiceImpl(DataCache dataCache, JobPostingService jobPostingService) {
        this.dataCache = dataCache;
        this.jobPostingService = jobPostingService;
    }

    @Override
    public Optional<Recruiter> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        ArrayList<User> users = dataCache.getUsers();
        for (User user : users) {
            // Check if user is Recruiter and matches the id
            if (id.equals(user.getId()) && user instanceof Recruiter) {
                return Optional.of((Recruiter) user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Recruiter updateRecruiter(Recruiter recruiter) {
        RecruiterValidator.validate(recruiter);

        Optional<Recruiter> existingOpt = findById(recruiter.getId());
        if (existingOpt.isEmpty()) {
            throw new BadRequestException("Recruiter with id " + recruiter.getId() + " not found.");
        }

        ArrayList<User> currentUsers = dataCache.getUsers();
        for (int i = 0; i < currentUsers.size(); i++) {
            User user = currentUsers.get(i);
            if (recruiter.getId().equals(user.getId()) && user instanceof Recruiter) {
                Recruiter existing = (Recruiter) user;
                existing.setFirstName(recruiter.getFirstName());
                existing.setLastName(recruiter.getLastName());
                existing.setEmail(recruiter.getEmail());
                existing.setPassword(recruiter.getPassword()); // Consider hashing password here
                existing.setCompany(recruiter.getCompany());
                existing.setRole(recruiter.getRole());

                currentUsers.set(i, existing);
                dataCache.setUsers(currentUsers);
                return existing;
            }
        }

        return recruiter;
    }

    @Override
    public void deleteRecruiter(String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Recruiter id cannot be empty");
        }

        ArrayList<User> currentUsers = dataCache.getUsers();
        boolean removed = false;

        for (int i = 0; i < currentUsers.size(); i++) {
            User user = currentUsers.get(i);
            if (id.equals(user.getId()) && user instanceof Recruiter) {
                currentUsers.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            throw new BadRequestException("Recruiter with id " + id + " not found.");
        }

        dataCache.setUsers(currentUsers);
    }

    @Override
    public ArrayList<Recruiter> getAllRecruiters() {
        ArrayList<Recruiter> recruiters = new ArrayList<>();
        for (User user : dataCache.getUsers()) {
            if (user instanceof Recruiter) {
                recruiters.add((Recruiter) user);
            }
        }
        return recruiters;
    }

    @Override
    public ArrayList<JobPosting> getAllRecruiterJobs(String recruiterId) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new BadRequestException("Recruiter ID required.");
        }

        ArrayList<JobPosting> jobPostings = dataCache.getJobs();
        ArrayList<JobPosting> result = new ArrayList<>();
        for (JobPosting job : jobPostings) {
            if (recruiterId.equals(job.getRecruiterId())) {
                result.add(job);
            }
        }
        return result;
    }

    @Override
    public JobPosting postJob(JobPosting job, String recruiterId) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new BadRequestException("Recruiter ID required for posting a job.");
        }

        job.setRecruiterId(recruiterId);
        return jobPostingService.createJob(job);
    }

    @Override
    public Recruiter save(Recruiter recruiter) {
        RecruiterValidator.validate(recruiter);

        ArrayList<User> currentUsers = dataCache.getUsers();
        for (User r : currentUsers) {
            if (r.getEmail() != null && recruiter.getEmail() != null &&
                    r.getEmail().equalsIgnoreCase(recruiter.getEmail())) {
                throw new BadRequestException("Recruiter with this email already exists.");
            }
        }

        currentUsers.add(recruiter);
        dataCache.setUsers(currentUsers);
        return recruiter;
    }
}