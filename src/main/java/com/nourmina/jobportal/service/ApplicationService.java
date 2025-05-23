package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.exception.ForbiddenException;
import com.nourmina.jobportal.exception.ResourceNotFoundException;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.Job;
import com.nourmina.jobportal.model.User;

import com.nourmina.jobportal.repository.ApplicationRepository;
import com.nourmina.jobportal.repository.JobRepository;
import com.nourmina.jobportal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // for spring
public class ApplicationService {

    // to get info from mongodb
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private JobRepository jobRepository;
    @Autowired private UserRepository userRepository;

    // Apply to a job
    public Application applyToJob(String jobId, String userId, String coverLetter) {
        // Verify user is a candidate
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!"CANDIDATE".equals(user.getRole())) {
            throw new ForbiddenException("Only candidates can apply to jobs");
        }

        // Check if job exists and is open
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (!optionalJob.isPresent()) {
            throw new ResourceNotFoundException("Job not found");
        }
        Job job = optionalJob.get();
        if (!"OPEN".equals(job.getStatus())) {
            throw new ForbiddenException("This job is not open for applications");
        }

        // Check if already applied
        if (applicationRepository.findByJobIdAndUserId(jobId, userId).isPresent()) {
            throw new BadRequestException("You have already applied to this job");
        }

        // Create and save the application
        Application application = new Application(jobId, userId, coverLetter);
        return applicationRepository.save(application);
    }

    // Get applications for a job (for recruiters)
    public List<Application> getApplicationsForJob(String jobId, String userId) {
        // Check if user is a recruiter
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!"RECRUITER".equals(user.getRole())) {
            throw new ForbiddenException("Only recruiters can view applications for a job");
        }

        // Check if the job belongs to this recruiter
        Optional<Job> optionalJob = jobRepository.findById(jobId);
        if (!optionalJob.isPresent()) {
            throw new ResourceNotFoundException("Job not found");
        }
        Job job = optionalJob.get();
        if (!job.getPostedBy().equals(userId)) {
            throw new ForbiddenException("You don't have permission to view these applications");
        }

        return applicationRepository.findByJobId(jobId);
    }

    // Get applications by a candidate
    public List<Application> getApplicationsForUser(String userId) {
        // Verify user exists and is a candidate
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!"CANDIDATE".equals(user.getRole())) {
            throw new ForbiddenException("Only candidates can view their applications");
        }

        return applicationRepository.findByUserId(userId);
    }

    // Get a single application
    public Application getApplication(String id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (!optionalApplication.isPresent()) {
            throw new ResourceNotFoundException("Application not found");
        }

        return optionalApplication.get();
    }

    // Update application status (for recruiters)
    public Application updateApplicationStatus(String id, String status, String userId) {
        // Verify recruiter access
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!"RECRUITER".equals(user.getRole())) {
            throw new ForbiddenException("Only recruiters can update applications");
        }

        // Get application
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (!optionalApplication.isPresent()) {
            throw new ResourceNotFoundException("Application not found");
        }
        Application application = optionalApplication.get();

        // Check if the job belongs to this recruiter
        Optional<Job> optionalJob = jobRepository.findById(application.getJobId());
        if (!optionalJob.isPresent()) {
            throw new ResourceNotFoundException("Job not found");
        }
        Job job = optionalJob.get();
        if (!job.getPostedBy().equals(userId)) {
            throw new ForbiddenException("You don't have permission to update this application");
        }

        // Update status
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    // Withdraw application (for candidates)
    public void withdrawApplication(String id, String userId) {
        // Check if user is a candidate
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!"CANDIDATE".equals(user.getRole())) {
            throw new ForbiddenException("Only candidates can withdraw applications");
        }

        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if (!optionalApplication.isPresent()) {
            throw new ResourceNotFoundException("Application not found");
        }
        Application application = optionalApplication.get();

        if (!application.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to withdraw this application");
        }

        application.setStatus("WITHDRAWN");
        applicationRepository.save(application);
    }

    // Check if candidate has already applied to a job
    public boolean hasApplied(String jobId, String userId) {
        return applicationRepository.findByJobIdAndUserId(jobId, userId).isPresent();
    }
}
