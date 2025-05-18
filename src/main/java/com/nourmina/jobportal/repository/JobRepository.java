package com.example.jobportal.repository;

import com.example.jobportal.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    // Basic queries
    Page<Job> findAll(Pageable pageable);
    Page<Job> findByPostedBy(String postedBy, Pageable pageable);
    Page<Job> findByStatus(String status, Pageable pageable);

    // Search queries
    Page<Job> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);
    Page<Job> findByJobTypeIgnoreCase(String jobType, Pageable pageable);

    // Combined query for search
    @Query("{'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'company': {$regex: ?0, $options: 'i'}}, {'location': {$regex: ?0, $options: 'i'}}]}")
    Page<Job> searchJobs(String keyword, Pageable pageable);
}