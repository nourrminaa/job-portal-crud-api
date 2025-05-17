package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    ArrayList<Candidate> findByEmail(String email);
    ArrayList<Candidate> findByLastNameIgnoreCase(String lastName);
    ArrayList<Candidate> findBySkillsIn(ArrayList<String> skills);
}
