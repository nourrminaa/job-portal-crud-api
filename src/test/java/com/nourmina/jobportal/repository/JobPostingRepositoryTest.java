package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=4.0.2")
public class JobPostingRepositoryTest {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository
        jobPostingRepository.deleteAll();

        // Create test job postings
        Job job1 = new Job(
                "Java Developer",
                "Experienced Java developer needed",
                "New York",
                new ArrayList<>(Arrays.asList("Java", "Spring", "MongoDB")),
                "recruiter1");
        job1.setCompany("TechCorp");
        job1.setSalary(80000.0);
        job1.setStatus("ACTIVE");

        Job job2 = new Job(
                "Senior Python Engineer",
                "Looking for Python expert with machine learning experience",
                "San Francisco",
                new ArrayList<>(Arrays.asList("Python", "Machine Learning", "TensorFlow")),
                "recruiter2");
        job2.setCompany("AI Solutions");
        job2.setSalary(120000.0);
        job2.setStatus("ACTIVE");

        Job job3 = new Job(
                "Frontend Developer",
                "Frontend developer with React experience",
                "New York",
                new ArrayList<>(Arrays.asList("JavaScript", "React", "CSS")),
                "recruiter1");
        job3.setCompany("TechCorp");
        job3.setSalary(75000.0);
        job3.setStatus("ACTIVE");

        jobPostingRepository.saveAll(Arrays.asList(job1, job2, job3));
    }

    @Test
    public void shouldFindJobsByRecruiterId() {
        // When
        List<Job> jobs = jobPostingRepository.findByRecruiterId("recruiter1");

        // Then
        assertThat(jobs).hasSize(2);
        assertThat(jobs).extracting(Job::getCompany).containsOnly("TechCorp");
    }

    @Test
    public void shouldFindJobsByTitleContaining() {
        // When
        List<Job> jobs = jobPostingRepository.findByTitleContainingIgnoreCase("java");

        // Then
        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getTitle()).isEqualTo("Java Developer");
    }

    @Test
    public void shouldFindJobsByLocation() {
        // When
        List<Job> jobs = jobPostingRepository.findByLocationContainingIgnoreCase("New York");

        // Then
        assertThat(jobs).hasSize(2);
        assertThat(jobs).extracting(Job::getTitle).containsExactlyInAnyOrder("Java Developer", "Frontend Developer");
    }

    @Test
    public void shouldFindJobsByRequiredSkill() {
        // When
        List<Job> jobs = jobPostingRepository.findByRequiredSkillsContaining("Java");

        // Then
        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getTitle()).isEqualTo("Java Developer");
    }

    @Test
    public void shouldSearchByKeyword() {
        // When
        Page<Job> jobs = jobPostingRepository.searchByKeyword("developer", PageRequest.of(0, 10));

        // Then
        assertThat(jobs.getContent()).hasSize(2);
        assertThat(jobs.getContent()).extracting(Job::getTitle).containsExactlyInAnyOrder("Java Developer", "Frontend Developer");
    }
}