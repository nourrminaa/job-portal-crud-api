package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    // MongoRepository<Application, String> --> provides CRUD operations for Application entity
    ArrayList<Application> findByCandidateId(String candidateId);
    ArrayList<Application> findByJobPostingId(String jobPostingId);
}
