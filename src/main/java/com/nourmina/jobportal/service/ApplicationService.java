package com.nourmina.jobportal.service;

import com.nourmina.jobportal.dto.ApplicationDTO;
import com.nourmina.jobportal.dto.ApplicationResponseDTO;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.repository.ApplicationRepository;
import com.nourmina.jobportal.repository.CandidateRepository;
import com.nourmina.jobportal.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Page<Application> getAllApplicationsWithPagination(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }

    public Application getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    public List<ApplicationResponseDTO> getApplicationsByCandidate(String candidateId) {
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);
        return mapToApplicationResponseDTOs(applications);
    }

    public Page<ApplicationResponseDTO> getApplicationsByCandidateWithPagination(String candidateId, Pageable pageable) {
        // This implementation is a bit inefficient as MongoDB repository doesn't directly support
        // converting the results to DTOs with pagination. In a production environment, you'd want to optimize this.

        // Get all applications for the candidate
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);

        // Convert to DTOs
        List<ApplicationResponseDTO> dtos = mapToApplicationResponseDTOs(applications);

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());

        // If start exceeds list size, return empty page
        if (start >= dtos.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, dtos.size());
        }

        // Create sublist for current page
        List<ApplicationResponseDTO> pageContent = dtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    public List<ApplicationResponseDTO> getApplicationsByJobPosting(String jobPostingId) {
        List<Application> applications = applicationRepository.findByJobPostingId(jobPostingId);
        return mapToApplicationResponseDTOs(applications);
    }

    public Page<ApplicationResponseDTO> getApplicationsByJobPostingWithPagination(String jobPostingId, Pageable pageable) {
        // This implementation is a bit inefficient as MongoDB repository doesn't directly support
        // converting the results to DTOs with pagination. In a production environment, you'd want to optimize this.

        // Get all applications for the job posting
        List<Application> applications = applicationRepository.findByJobPostingId(jobPostingId);

        // Convert to DTOs
        List<ApplicationResponseDTO> dtos = mapToApplicationResponseDTOs(applications);

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());

        // If start exceeds list size, return empty page
        if (start >= dtos.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, dtos.size());
        }

        // Create sublist for current page
        List<ApplicationResponseDTO> pageContent = dtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, dtos.size());
    }

    public List<Application> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    public Application apply(ApplicationDTO applicationDTO) {
        // Verify candidate exists
        Candidate candidate = candidateRepository.findById(applicationDTO.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + applicationDTO.getCandidateId()));

        // Verify job posting exists
        JobPosting jobPosting = jobPostingRepository.findById(applicationDTO.getJobPostingId())
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found with id: " + applicationDTO.getJobPostingId()));

        // Check if application already exists
        Optional<Application> existingApplication = applicationRepository
                .findByCandidateIdAndJobPostingId(applicationDTO.getCandidateId(), applicationDTO.getJobPostingId());

        if (existingApplication.isPresent()) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        // Create application
        Application application = new Application(
                applicationDTO.getCandidateId(),
                applicationDTO.getJobPostingId(),
                Application.STATUS_APPLIED
        );

        application.setAppliedDate(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public Application updateApplicationStatus(String id, String status) {
        Application application = getApplicationById(id);

        // Validate status
        if (!status.equals(Application.STATUS_APPLIED) &&
                !status.equals(Application.STATUS_SHORTLISTED) &&
                !status.equals(Application.STATUS_REJECTED) &&
                !status.equals(Application.STATUS_PENDING)) {
            throw new IllegalArgumentException("Invalid application status: " + status);
        }

        application.setStatus(status);
        return applicationRepository.save(application);
    }

    public void deleteApplication(String id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }

    // Helper method to map Application entities to ApplicationResponseDTOs
    private List<ApplicationResponseDTO> mapToApplicationResponseDTOs(List<Application> applications) {
        List<ApplicationResponseDTO> responseDTOs = new ArrayList<>();

        for (Application application : applications) {
            ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
            responseDTO.setId(application.getId());
            responseDTO.setCandidateId(application.getCandidateId());
            responseDTO.setJobPostingId(application.getJobPostingId());
            responseDTO.setStatus(application.getStatus());
            responseDTO.setAppliedDate(application.getAppliedDate());

            // Add job posting details
            JobPosting jobPosting = jobPostingRepository.findById(application.getJobPostingId()).orElse(null);
            if (jobPosting != null) {
                responseDTO.setJobTitle(jobPosting.getTitle());
                responseDTO.setCompany(jobPosting.getCompany());
            }

            // Add candidate details
            Candidate candidate = candidateRepository.findById(application.getCandidateId()).orElse(null);
            if (candidate != null) {
                responseDTO.setCandidateName(candidate.getFirstName() + " " + candidate.getLastName());
            }

            responseDTOs.add(responseDTO);
        }

        return responseDTOs;
    }
}