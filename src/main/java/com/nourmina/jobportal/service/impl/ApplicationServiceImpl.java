package com.nourmina.jobportal.service.impl;

// Importing the Application model (represents a job application)
import com.nourmina.jobportal.model.Application;

// Repository to interact with the Application data in the database
import com.nourmina.jobportal.repository.ApplicationRepository;

// Interface that this class implements (defines what services it provides)
import com.nourmina.jobportal.service.ApplicationService;

// Custom exception thrown if an application is not found
import com.nourmina.jobportal.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service // Marks this class as a Spring service (used for business logic)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository; // Database access for applications

    @Autowired // Spring will automatically inject the repository here
    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public Application updateApplicationStatus(String appId, String status) {
        // Find the application by ID, or throw an error if it doesn't exist
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        // Update the status of the application (e.g., from PENDING to ACCEPTED)
        application.setStatus(status);

        // Save and return the updated application
        return applicationRepository.save(application);
    }

    @Override
    public ArrayList<Application> findApplicationsByCandidateId(String candidateId) {
        // Return all applications submitted by the candidate with this ID
        return new ArrayList<Application>(applicationRepository.findByCandidateId(candidateId));
    }
}
