package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Application;

import java.util.List;

public interface ApplicationService {
    Application updateApplicationStatus(String appId, String status);
    List<Application> findApplicationsByCandidateId(String candidateId);
}
