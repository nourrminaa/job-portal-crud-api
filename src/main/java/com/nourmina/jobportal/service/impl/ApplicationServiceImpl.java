package com.nourmina.jobportal.service.impl;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.service.ApplicationService;
import com.nourmina.jobportal.cache.DataCache;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final DataCache dataCache;

    public ApplicationServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public void updateApplicationStatus(String applicationId, String newStatus) {
        for (Application application : dataCache.getApplications()) {
            if (application.getId() != null && application.getId().equals(applicationId)) {
                application.setStatus(newStatus);
                return;
            }
        }
        System.out.println("Application not found.");
    }

    @Override
    public ArrayList<Application> findApplicationsByCandidateId(String candidateId) {
        ArrayList<Application> result = new ArrayList<>();
        for (Application app : dataCache.getApplications()) {
            // Null-safe check to avoid NullPointerException
            if (app.getCandidateId() != null && app.getCandidateId().equals(candidateId)) {
                result.add(app);
            }
        }
        return result;
    }

    @Override
    public void loadApplications(ArrayList<Application> apps) {
        dataCache.getApplications().clear();
        dataCache.getApplications().addAll(apps);
    }

    @Override
    public ArrayList<Application> getAllApplications() {
        return new ArrayList<>(dataCache.getApplications());
    }
}
