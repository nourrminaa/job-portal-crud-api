package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Application;

import java.util.ArrayList;
import java.util.Optional;

public interface ApplicationService {
    Application save(Application application);
    ArrayList<Application> getApplicationsByJobId(String jobId);
    ArrayList<Application> getApplicationsByCandidateId(String candidateId);
    void withdrawApplication(String applicationId);
    Optional<Application> findById(String applicationId);
}
