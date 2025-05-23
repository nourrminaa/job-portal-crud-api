package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Job;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.repository.JobRepository;
import com.nourmina.jobportal.repository.UserRepository;

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

    @Autowired private JobRepository jobRepository;
    @Autowired private UserRepository userRepository;

    // Create a new job (RECRUITER ONLY)
    public Job createJob(Job job, String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("Only recruiters can post jobs");
        }

        job.setPostedBy(userId);
        job.setPostedAt(LocalDateTime.now());
        job.setStatus("OPEN");

        if (job.getCompany() == null || job.getCompany().isEmpty()) {
            job.setCompany(user.getCompany());
        }

        return jobRepository.save(job);
    }

    // Get all jobs with pagination and sorting (Everyone)
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

    // Get jobs posted by a specific recruiter (RECRUITER ONLY)
    public Page<Job> getJobsByRecruiter(String userId, int page, int size, String sortBy, String sortDirection) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("User is not a recruiter");
        }

        Sort sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return jobRepository.findByPostedBy(userId, pageable);
    }

    // Get a job by ID (open to all)
    public Optional<Job> getJob(String id) {
        return jobRepository.findById(id);
    }

    // Update a job (RECRUITER ONLY + must be owner)
    public Job updateJob(Job job, String userId) {
        Optional<Job> optionalJob = jobRepository.findById(job.getId());
        if (!optionalJob.isPresent()) {
            throw new RuntimeException("Job not found");
        }
        Job existingJob = optionalJob.get();

        if (!existingJob.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this job");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("Only recruiters can update jobs");
        }

        job.setPostedBy(existingJob.getPostedBy());
        job.setPostedAt(existingJob.getPostedAt());

        return jobRepository.save(job);
    }

    // Delete a job (RECRUITER ONLY + must be owner)
    public void deleteJob(String id, String userId) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (!optionalJob.isPresent()) {
            throw new RuntimeException("Job not found");
        }
        Job job = optionalJob.get();

        if (!job.getPostedBy().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this job");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optionalUser.get();

        if (!"RECRUITER".equals(user.getRole())) {
            throw new IllegalArgumentException("Only recruiters can delete jobs");
        }

        jobRepository.deleteById(id);
    }

    // Search jobs (open to all)
    public Page<Job> searchJobs(String keyword, String location, String jobType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (keyword != null && !keyword.isEmpty()) {
            return jobRepository.searchJobs(keyword, pageable);
        }

        if (location != null && !location.isEmpty()) {
            return jobRepository.findByLocationContainingIgnoreCase(location, pageable);
        }

        if (jobType != null && !jobType.isEmpty()) {
            return jobRepository.findByJobTypeIgnoreCase(jobType, pageable);
        }

        return jobRepository.findByStatus("OPEN", pageable);
    }
}
