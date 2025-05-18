package com.nourmina.jobportal.service;

import com.nourmina.jobportal.dto.JobPostingDTO;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.repository.JobPostingRepository;
import com.nourmina.jobportal.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    public Page<JobPosting> getAllJobPostings(Pageable pageable) {
        return jobPostingRepository.findAll(pageable);
    }

    public JobPosting getJobPostingById(String id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + id));
    }

    public List<JobPosting> getJobPostingsByRecruiter(String recruiterId) {
        return jobPostingRepository.findByRecruiterId(recruiterId);
    }

    public List<JobPosting> searchJobPostings(String keyword, Pageable pageable) {
        return jobPostingRepository.searchByKeyword(keyword, pageable).getContent();
    }

    public List<JobPosting> searchJobPostingsByLocation(String location) {
        return jobPostingRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<JobPosting> searchJobPostingsBySkill(String skill) {
        return jobPostingRepository.findByRequiredSkillsContaining(skill);
    }

    public JobPosting createJobPosting(JobPostingDTO jobPostingDTO, String recruiterId) {
        // Verify recruiter exists
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with id: " + recruiterId));

        // Create job posting entity from DTO
        JobPosting jobPosting = new JobPosting(
                jobPostingDTO.getTitle(),
                jobPostingDTO.getDescription(),
                jobPostingDTO.getLocation(),
                jobPostingDTO.getRequiredSkills(),
                recruiterId
        );

        jobPosting.setSalary(jobPostingDTO.getSalary());
        jobPosting.setCompany(recruiter.getCompany()); // Use recruiter's company
        jobPosting.setPostDate(LocalDateTime.now());
        jobPosting.setStatus("ACTIVE");

        return jobPostingRepository.save(jobPosting);
    }

    public JobPosting updateJobPosting(String id, JobPostingDTO jobPostingDTO) {
        JobPosting jobPosting = getJobPostingById(id);

        // Update fields
        jobPosting.setTitle(jobPostingDTO.getTitle());
        jobPosting.setDescription(jobPostingDTO.getDescription());
        jobPosting.setLocation(jobPostingDTO.getLocation());
        jobPosting.setRequiredSkills(jobPostingDTO.getRequiredSkills());
        jobPosting.setSalary(jobPostingDTO.getSalary());

        if (jobPostingDTO.getStatus() != null) {
            jobPosting.setStatus(jobPostingDTO.getStatus());
        }

        return jobPostingRepository.save(jobPosting);
    }

    public void deleteJobPosting(String id) {
        JobPosting jobPosting = getJobPostingById(id);
        jobPostingRepository.delete(jobPosting);
    }

    /**
     * Get the recruiter ID from an email address
     * @param email the email address
     * @return the recruiter ID
     */
    public String getRecruiterIdFromEmail(String email) {
        return recruiterRepository.findByEmail(email)
                .map(Recruiter::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found with email: " + email));
    }
}