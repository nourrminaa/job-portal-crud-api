package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.util.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final DataCache dataCache;
    private final IdGenerator idGenerator;

    public ApplicationServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
        this.idGenerator = new IdGenerator();
    }

    @Override
    // Create Application
    public Application save(Application application) {
        if (application == null) {
            throw new BadRequestException("Application cannot be null");
        }

        if (application.getCandidateId() == null || application.getCandidateId().isBlank()) {
            throw new BadRequestException("Candidate ID is required");
        }

        if (application.getJobPostingId() == null || application.getJobPostingId().isBlank()) {
            throw new BadRequestException("Job ID is required");
        }

        // Generate a new ID for the application
        application.setId(idGenerator.generateId("APP"));

        ArrayList<Application> currentApplications = dataCache.getApplications();
        currentApplications.add(application);
        dataCache.setApplications(currentApplications);
        return application;
    }

    // Delete
    @Override
    public void withdrawApplication(String applicationId) {
        if (applicationId == null || applicationId.isBlank()) {
            throw new BadRequestException("Application ID is required");
        }

        ArrayList<Application> currentApplications = dataCache.getApplications();
        boolean removed = currentApplications.removeIf(app -> applicationId.equals(app.getId()));

        if (!removed) {
            throw new BadRequestException("Application not found.");
        }

        dataCache.setApplications(currentApplications);
    }

    @Override
    public ArrayList<Application> getApplicationsByJobId(String jobId) {
        if (jobId == null || jobId.isBlank()) {
            throw new BadRequestException("Job ID is required");
        }

        ArrayList<Application> result = new ArrayList<>();
        for (Application app : dataCache.getApplications()) {
            if (jobId.equals(app.getJobPostingId())) {
                result.add(app);
            }
        }
        return result;
    }

    @Override
    public ArrayList<Application> getApplicationsByCandidateId(String candidateId) {
        if (candidateId == null || candidateId.isBlank()) {
            throw new BadRequestException("Candidate ID is required");
        }

        ArrayList<Application> result = new ArrayList<>();
        for (Application app : dataCache.getApplications()) {
            if (candidateId.equals(app.getCandidateId())) {
                result.add(app);
            }
        }
        return result;
    }

    @Override
    public Optional<Application> findById(String applicationId) {
        if (applicationId == null || applicationId.isBlank()) {
            return Optional.empty();
        }

        return dataCache.getApplications().stream()
                .filter(app -> applicationId.equals(app.getId()))
                .findFirst();
    }
}