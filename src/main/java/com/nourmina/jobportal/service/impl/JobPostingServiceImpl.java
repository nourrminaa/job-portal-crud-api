package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.util.IdGenerator;
import com.nourmina.jobportal.validation.JobPostingValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final DataCache dataCache;
    private IdGenerator idGenerator;

    public JobPostingServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public JobPosting createJob(JobPosting job) {
        JobPostingValidator.validate(job);

        job.setId(idGenerator.generateId("JOB"));

        ArrayList<JobPosting> currentJobs = dataCache.getJobs();
        currentJobs.add(job);
        dataCache.setJobs(currentJobs);
        return job;
    }

    @Override
    public ArrayList<JobPosting> getAllJobs() {
        return new ArrayList<>(dataCache.getJobs());
    }

    @Override
    public ArrayList<JobPosting> getJobsByRecruiterId(String recruiterId) {
        if (recruiterId == null || recruiterId.isBlank()) {
            throw new BadRequestException("Recruiter ID is required.");
        }
        return dataCache.getJobs().stream()
                .filter(job -> recruiterId.equals(job.getRecruiterId()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public JobPosting updateJob(JobPosting job) {
        JobPostingValidator.validate(job);

        if (job.getId() == null || job.getId().isBlank()) {
            throw new BadRequestException("Job ID is required for update.");
        }

        ArrayList<JobPosting> currentJobs = dataCache.getJobs();
        Optional<JobPosting> existingOpt = currentJobs.stream()
                .filter(j -> job.getId().equals(j.getId()))
                .findFirst();

        if (existingOpt.isEmpty()) {
            throw new BadRequestException("Job with ID " + job.getId() + " not found.");
        }

        JobPosting existing = existingOpt.get();
        int index = currentJobs.indexOf(existing);

        existing.setTitle(job.getTitle());
        existing.setCompany(job.getCompany());
        existing.setDescription(job.getDescription());
        existing.setRequiredSkills(job.getRequiredSkills());
        existing.setRecruiterId(job.getRecruiterId());
        existing.setLocation(job.getLocation());
        existing.setSalary(job.getSalary());

        currentJobs.set(index, existing);
        dataCache.setJobs(currentJobs);

        return existing;
    }

    @Override
    public void deleteJob(String jobId) {
        if (jobId == null || jobId.isBlank()) {
            throw new BadRequestException("Job ID cannot be empty.");
        }

        ArrayList<JobPosting> currentJobs = dataCache.getJobs();
        boolean removed = currentJobs.removeIf(job -> jobId.equals(job.getId()));

        if (!removed) {
            throw new BadRequestException("Job with ID " + jobId + " not found.");
        }

        dataCache.setJobs(currentJobs);
    }

    @Override
    public Optional<JobPosting> findById(String jobId) {
        for (JobPosting job : getAllJobs()) {
            if (job.getId().equals(jobId)) {
                return Optional.of(job);
            }
        }
        return Optional.empty();
    }
}