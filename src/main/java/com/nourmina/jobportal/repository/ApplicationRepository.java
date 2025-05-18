package com.example.jobportal.repository;

import com.example.jobportal.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByJobId(String jobId);
    List<Application> findByUserId(String userId);
    Optional<Application> findByJobIdAndUserId(String jobId, String userId);
    long countByJobId(String jobId);
}