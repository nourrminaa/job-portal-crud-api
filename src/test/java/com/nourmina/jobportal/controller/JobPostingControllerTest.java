package com.nourmina.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nourmina.jobportal.model.Job;
import com.nourmina.jobportal.service.JobPostingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobPostingController.class)
public class JobPostingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobPostingService jobPostingService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private Job jobPosting;
    private JobPostingDTO jobPostingDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // Set up test job posting
        ArrayList<String> skills = new ArrayList<>(Arrays.asList("Java", "Spring", "MongoDB"));
        jobPosting = new Job("Java Developer", "Job description", "New York", skills, "recruiter123");
        jobPosting.setId("job123");
        jobPosting.setCompany("TechCorp");
        jobPosting.setSalary(80000.0);
        jobPosting.setStatus("ACTIVE");

        // Set up test job posting DTO
        jobPostingDTO = new JobPostingDTO();
        jobPostingDTO.setTitle("Java Developer");
        jobPostingDTO.setDescription("Job description");
        jobPostingDTO.setLocation("New York");
        jobPostingDTO.setRequiredSkills(skills);
        jobPostingDTO.setSalary(80000.0);
        jobPostingDTO.setCompany("TechCorp");
    }

    @Test
    public void shouldGetAllJobPostings() throws Exception {
        List<Job> jobPostings = Arrays.asList(jobPosting);
        PageImpl<Job> page = new PageImpl<>(jobPostings);

        when(jobPostingService.getAllJobPostings(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/job-postings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Java Developer")));
    }

    @Test
    public void shouldGetJobPostingById() throws Exception {
        when(jobPostingService.getJobPostingById("job123")).thenReturn(jobPosting);

        mockMvc.perform(get("/api/job-postings/job123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("job123")))
                .andExpect(jsonPath("$.title", is("Java Developer")));
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    public void shouldCreateJobPosting() throws Exception {
        when(jobPostingService.getRecruiterIdFromEmail(any())).thenReturn("recruiter123");
        when(jobPostingService.createJobPosting(any(JobPostingDTO.class), eq("recruiter123"))).thenReturn(jobPosting);
        when(securityService.isJobPostingOwner(any())).thenReturn(true);

        mockMvc.perform(post("/api/job-postings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobPostingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.title", is("Java Developer")));
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    public void shouldUpdateJobPosting() throws Exception {
        when(securityService.isJobPostingOwner("job123")).thenReturn(true);
        when(jobPostingService.updateJobPosting(eq("job123"), any(JobPostingDTO.class))).thenReturn(jobPosting);

        mockMvc.perform(put("/api/job-postings/job123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobPostingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.title", is("Java Developer")));
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    public void shouldDeleteJobPosting() throws Exception {
        when(securityService.isJobPostingOwner("job123")).thenReturn(true);

        mockMvc.perform(delete("/api/job-postings/job123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}

// Path: src/test/java/com/nourmina/jobportal/controller/ApplicationControllerTest.java
package com.nourmina.jobportal.controller;

import ObjectMapper;
import com.nourmina.jobportal.dto.ApplicationDTO;
import com.nourmina.jobportal.dto.ApplicationResponseDTO;
import com.nourmina.jobportal.model.Application;
import JwtTokenProvider;
import com.nourmina.jobportal.service.ApplicationService;
import SecurityService;
import BeforeEach;
import Test;
import Autowired;
import WebMvcTest;
import MockBean;
import MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import WithMockUser;
import MockMvc;

import java.time.LocalDateTime;
import Arrays;
import List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

