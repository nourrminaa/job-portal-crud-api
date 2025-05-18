package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.Job;
import com.nourmina.jobportal.repository.ApplicationRepository;
import com.nourmina.jobportal.repository.JobPostingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobPostingRepository jobPostingRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Application application;
    private ApplicationDTO applicationDTO;
    private Candidate candidate;
    private Job jobPosting;

    @BeforeEach
    public void setUp() {
        // Set up test application
        application = new Application("candidate123", "job123", Application.STATUS_APPLIED);
        application.setId("app123");
        application.setAppliedDate(LocalDateTime.now());

        // Set up test application DTO
        applicationDTO = new ApplicationDTO();
        applicationDTO.setCandidateId("candidate123");
        applicationDTO.setJobPostingId("job123");
        applicationDTO.setStatus(Application.STATUS_APPLIED);

        // Set up test candidate
        candidate = new Candidate("John", "Doe", "john.doe@example.com", "password", "resume.pdf", new ArrayList<>());
        candidate.setId("candidate123");

        // Set up test job posting
        ArrayList<String> skills = new ArrayList<>(Arrays.asList("Java", "Spring", "MongoDB"));
        jobPosting = new Job("Java Developer", "Job description", "New York", skills, "recruiter123");
        jobPosting.setId("job123");
        jobPosting.setCompany("TechCorp");
    }

    @Test
    public void shouldGetAllApplications() {
        // Given
        List<Application> applications = Arrays.asList(application);
        when(applicationRepository.findAll()).thenReturn(applications);

        // When
        List<Application> result = applicationService.getAllApplications();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("app123");
    }

    @Test
    public void shouldGetApplicationById() {
        // Given
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(application));

        // When
        Application result = applicationService.getApplicationById("app123");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("app123");
    }

    @Test
    public void shouldThrowExceptionWhenApplicationNotFound() {
        // Given
        when(applicationRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> applicationService.getApplicationById("nonexistent"));
    }

    @Test
    public void shouldGetApplicationsByCandidate() {
        // Given
        List<Application> applications = Arrays.asList(application);
        when(applicationRepository.findByCandidateId("candidate123")).thenReturn(applications);
        when(jobPostingRepository.findById("job123")).thenReturn(Optional.of(jobPosting));
        when(candidateRepository.findById("candidate123")).thenReturn(Optional.of(candidate));

        // When
        List<ApplicationResponseDTO> result = applicationService.getApplicationsByCandidate("candidate123");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCandidateId()).isEqualTo("candidate123");
        assertThat(result.get(0).getJobTitle()).isEqualTo("Java Developer");
    }

    @Test
    public void shouldGetApplicationsByJobPosting() {
        // Given
        List<Application> applications = Arrays.asList(application);
        when(applicationRepository.findByJobPostingId("job123")).thenReturn(applications);
        when(jobPostingRepository.findById("job123")).thenReturn(Optional.of(jobPosting));
        when(candidateRepository.findById("candidate123")).thenReturn(Optional.of(candidate));

        // When
        List<ApplicationResponseDTO> result = applicationService.getApplicationsByJobPosting("job123");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getJobPostingId()).isEqualTo("job123");
        assertThat(result.get(0).getCandidateName()).isEqualTo("John Doe");
    }

    @Test
    public void shouldApplyForJob() {
        // Given
        when(candidateRepository.findById("candidate123")).thenReturn(Optional.of(candidate));
        when(jobPostingRepository.findById("job123")).thenReturn(Optional.of(jobPosting));
        when(applicationRepository.findByCandidateIdAndJobPostingId("candidate123", "job123"))
                .thenReturn(Optional.empty());
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        // When
        Application result = applicationService.apply(applicationDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCandidateId()).isEqualTo("candidate123");
        assertThat(result.getJobPostingId()).isEqualTo("job123");
        assertThat(result.getStatus()).isEqualTo(Application.STATUS_APPLIED);
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    public void shouldThrowExceptionWhenAlreadyApplied() {
        // Given
        when(candidateRepository.findById("candidate123")).thenReturn(Optional.of(candidate));
        when(jobPostingRepository.findById("job123")).thenReturn(Optional.of(jobPosting));
        when(applicationRepository.findByCandidateIdAndJobPostingId("candidate123", "job123"))
                .thenReturn(Optional.of(application));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> applicationService.apply(applicationDTO));
    }

    @Test
    public void shouldUpdateApplicationStatus() {
        // Given
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        // When
        Application result = applicationService.updateApplicationStatus("app123", Application.STATUS_SHORTLISTED);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Application.STATUS_SHORTLISTED);
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    public void shouldThrowExceptionForInvalidStatus() {
        // Given
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(application));

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> applicationService.updateApplicationStatus("app123", "INVALID_STATUS"));
    }

    @Test
    public void shouldDeleteApplication() {
        // Given
        when(applicationRepository.findById("app123")).thenReturn(Optional.of(application));
        doNothing().when(applicationRepository).delete(application);

        // When
        applicationService.deleteApplication("app123");

        // Then
        verify(applicationRepository, times(1)).delete(application);
    }
}