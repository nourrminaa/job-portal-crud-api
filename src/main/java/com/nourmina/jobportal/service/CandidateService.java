package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.JobPosting;

import java.util.ArrayList;
import java.util.Optional;

public interface CandidateService {
    Candidate save(Candidate candidate);
    void applyToJob(String candidateId, String jobId);
    ArrayList<JobPosting> viewAllAvailableJobs();
    ArrayList<JobPosting> viewAppliedJobs(String candidateId);
    Candidate updateProfile(Candidate candidate);
    void deleteAccount(String candidateId);
    Optional<Candidate> findById(String candidateId);
}
