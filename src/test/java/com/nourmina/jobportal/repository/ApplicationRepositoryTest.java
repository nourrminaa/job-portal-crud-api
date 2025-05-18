package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=4.0.2")
public class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @BeforeEach
    public void setUp() {
        // Clear the repository
        applicationRepository.deleteAll();

        // Create test applications
        Application app1 = new Application("candidate1", "job1", Application.STATUS_APPLIED);
        Application app2 = new Application("candidate1", "job2", Application.STATUS_SHORTLISTED);
        Application app3 = new Application("candidate2", "job1", Application.STATUS_PENDING);
        Application app4 = new Application("candidate3", "job3", Application.STATUS_REJECTED);

        applicationRepository.saveAll(Arrays.asList(app1, app2, app3, app4));
    }

    @Test
    public void shouldFindApplicationsByCandidateId() {
        // When
        List<Application> applications = applicationRepository.findByCandidateId("candidate1");

        // Then
        assertThat(applications).hasSize(2);
        assertThat(applications).extracting(Application::getJobPostingId).containsExactlyInAnyOrder("job1", "job2");
    }

    @Test
    public void shouldFindApplicationsByJobPostingId() {
        // When
        List<Application> applications = applicationRepository.findByJobPostingId("job1");

        // Then
        assertThat(applications).hasSize(2);
        assertThat(applications).extracting(Application::getCandidateId).containsExactlyInAnyOrder("candidate1", "candidate2");
    }

    @Test
    public void shouldFindApplicationsByStatus() {
        // When
        List<Application> applications = applicationRepository.findByStatus(Application.STATUS_APPLIED);

        // Then
        assertThat(applications).hasSize(1);
        assertThat(applications.get(0).getCandidateId()).isEqualTo("candidate1");
        assertThat(applications.get(0).getJobPostingId()).isEqualTo("job1");
    }

    @Test
    public void shouldFindApplicationByCandidateIdAndJobPostingId() {
        // When
        Optional<Application> application = applicationRepository.findByCandidateIdAndJobPostingId("candidate1", "job1");

        // Then
        assertThat(application).isPresent();
        assertThat(application.get().getStatus()).isEqualTo(Application.STATUS_APPLIED);
    }

    @Test
    public void shouldCountApplicationsByJobPostingId() {
        // When
        long count = applicationRepository.countByJobPostingId("job1");

        // Then
        assertThat(count).isEqualTo(2);
    }
}