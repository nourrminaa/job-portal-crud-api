package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
    List<JobPosting> findByRecruiterId(String recruiterId);

    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<JobPosting> findByTitleContainingIgnoreCase(String title);

    @Query("{'location': {$regex: ?0, $options: 'i'}}")
    List<JobPosting> findByLocationContainingIgnoreCase(String location);

    @Query("{'requiredSkills': {$in: [?0]}}")
    List<JobPosting> findByRequiredSkillsContaining(String skill);

    @Query("{$or: [{'title': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
    Page<JobPosting> searchByKeyword(String keyword, Pageable pageable);
}
