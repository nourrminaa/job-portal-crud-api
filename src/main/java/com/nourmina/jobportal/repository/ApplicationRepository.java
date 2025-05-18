package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByCandidateId(String candidateId);
    List<Application> findByJobPostingId(String jobPostingId);
    List<Application> findByStatus(String status);
    Optional<Application> findByCandidateIdAndJobPostingId(String candidateId, String jobPostingId);
    long countByJobPostingId(String jobPostingId);
    long countByCandidateId(String candidateId);
}