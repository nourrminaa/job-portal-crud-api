package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface RecruiterRepository extends MongoRepository<Recruiter, String> {
    ArrayList<Recruiter> findByEmail(String email);
    ArrayList<Recruiter> findByCompanyIgnoreCase(String company);
}
