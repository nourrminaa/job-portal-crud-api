package com.example.jobportal.service;

import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new job
    public Job createJob(Job job, String userId) {
        // Verify user is a recruiter
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("Only recruiters can post jobs");
        }

        job.setPostedBy(userId);
        job.setPostedAt(LocalDateTime.now());
        job.setStatus("OPEN");

        // Set company from user profile if not provided
        if (job.getCompany() == null || job.getCompany().isEmpty()) {
            job.setCompany(user.getCompany());
        }

        return jobRepository.save(job);
    }

    // Get all jobs with pagination and sorting
    public Page<Job> listJobs(String status, int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (status != null && !status.isEmpty()) {
            return jobRepository.findByStatus(status, pageable);
        }

        return jobRepository.findAll(pageable);
    }

    // Get jobs posted by a specific recruiter
    public Page<Job> getJobsByRecruiter(String userId, int page, int size, String sortBy, String sortDirection) {
        // Verify user is a recruiter
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("User is not a recruiter");
        }

        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return jobRepository.findByPostedBy(userId, pageable);
    }

    // Get a job by ID
    public Optional<Job> getJob(String id) {
        return jobRepository.findById(id);
    }

    // Update a job
    public Job updateJob(Job job, String userId) {
        // Verify job exists and user is the owner
        Job existingJob = jobRepository.findById(job.getId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!existingJob.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this job");
        }

        // Update fields but keep original poster and posted date
        job.setPostedBy(existingJob.getPostedBy());
        job.setPostedAt(existingJob.getPostedAt());

        return jobRepository.save(job);
    }

    // Delete a job
    public void deleteJob(String id, String userId) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this job");
        }

        jobRepository.deleteById(id);
    }

    // Search jobs
    public Page<Job> searchJobs(String keyword, String location, String jobType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // If keyword is provided
        if (keyword != null && !keyword.isEmpty()) {
            return jobRepository.searchJobs(keyword, pageable);
        }

        // If location is provided
        if (location != null && !location.isEmpty()) {
            return jobRepository.findByLocationContainingIgnoreCase(location, pageable);
        }

        // If job type is provided
        if (jobType != null && !jobType.isEmpty()) {
            return jobRepository.findByJobTypeIgnoreCase(jobType, pageable);
        }

        // Default: return all open jobs
        return jobRepository.findByStatus("OPEN", pageable);
    }
}