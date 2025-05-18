package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Application;
import java.util.ArrayList;

public interface ApplicationService {
    void updateApplicationStatus(String appId, String status);
    ArrayList<Application> findApplicationsByCandidateId(String candidateId);
    void loadApplications(ArrayList<Application> apps);
    ArrayList<Application> getAllApplications();
}
