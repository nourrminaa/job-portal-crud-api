package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.repository.ApplicationRepository;
import com.nourmina.jobportal.repository.JobPostingRepository;
import com.nourmina.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for handling security-related operations and checks
 */
@Service
public class SecurityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * Check if the currently authenticated user is the requested user
     * @param userId the ID of the user to check
     * @return true if the current user is the requested user
     */
    public boolean isCurrentUser(String userId) {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.isPresent() && currentUser.get().getId().equals(userId);
    }

    /**
     * Check if the currently authenticated user is the owner of the job posting
     * @param jobPostingId the ID of the job posting to check
     * @return true if the current user is the owner of the job posting
     */
    public boolean isJobPostingOwner(String jobPostingId) {
        Optional<User> currentUser = getCurrentUser();
        if (!currentUser.isPresent()) {
            return false;
        }

        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobPostingId);
        return jobPosting.isPresent() && jobPosting.get().getRecruiterId().equals(currentUser.get().getId());
    }

    /**
     * Check if the currently authenticated user is the recruiter of the job posting
     * @param jobPostingId the ID of the job posting to check
     * @return true if the current user is the recruiter of the job posting
     */
    public boolean isJobPostingRecruiter(String jobPostingId) {
        return isJobPostingOwner(jobPostingId);
    }

    /**
     * Check if the currently authenticated user is the recruiter of the job posting associated with an application
     * @param applicationId the ID of the application to check
     * @return true if the current user is the recruiter of the job posting
     */
    public boolean isRecruiterOfApplication(String applicationId) {
        Optional<User> currentUser = getCurrentUser();
        if (!currentUser.isPresent()) {
            return false;
        }

        Optional<Application> application = applicationRepository.findById(applicationId);
        if (!application.isPresent()) {
            return false;
        }

        Optional<JobPosting> jobPosting = jobPostingRepository.findById(application.get().getJobPostingId());
        return jobPosting.isPresent() && jobPosting.get().getRecruiterId().equals(currentUser.get().getId());
    }

    /**
     * Check if the currently authenticated user is the owner of the application
     * @param applicationId the ID of the application to check
     * @return true if the current user is the owner of the application
     */
    public boolean isApplicationOwner(String applicationId) {
        Optional<User> currentUser = getCurrentUser();
        if (!currentUser.isPresent()) {
            return false;
        }

        Optional<Application> application = applicationRepository.findById(applicationId);
        return application.isPresent() && application.get().getCandidateId().equals(currentUser.get().getId());
    }

    /**
     * Get the current user from the database
     * @return the current user
     */
    private Optional<User> getCurrentUser() {
        return Optional.empty(); // This will be implemented with Spring Security
    }

    /**
     * Get the ID of the current user
     * @param userDetails the UserDetails of the current user
     * @return the ID of the current user
     */
    public String getCurrentUserId(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .map(User::getId)
                .orElse(null);
    }
}
