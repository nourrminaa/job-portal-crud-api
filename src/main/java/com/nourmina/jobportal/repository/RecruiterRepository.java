package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends MongoRepository<Recruiter, String> {
    Optional<Recruiter> findByEmail(String email);
    List<Recruiter> findByCompany(String company);
}