package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.Application;

public interface CandidateService {
    Candidate registerCandidate(Candidate candidate);
    Candidate getCandidateById(String id);
    Candidate updateProfile(String id, Candidate updatedCandidate);
    Application applyToJob(String candidateId, String jobId);
}
