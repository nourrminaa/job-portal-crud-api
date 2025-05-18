package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.Candidate;

import java.util.Optional;

public interface CandidateService {
    Candidate registerCandidate(Candidate candidate);
    Optional<Candidate> findById(String id);
    Candidate updateProfile(String id, Candidate updatedCandidate);
    Application applyToJob(String candidateId, String jobId);
}
