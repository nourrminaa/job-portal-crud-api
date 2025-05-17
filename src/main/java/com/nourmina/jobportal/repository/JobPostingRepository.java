package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
    ArrayList<JobPosting> findByTitleContainingIgnoreCase(String keyword);
    ArrayList<JobPosting> findByRequiredSkillsIn(ArrayList<String> skills);
    ArrayList<JobPosting> findByLocation(String location);
}
