package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.JobPosting;
import java.util.ArrayList;

public interface JobPostingService {
    ArrayList<JobPosting> listJobs();
    ArrayList<JobPosting> searchJobs(String keyword);
    void loadJobPostings(ArrayList<JobPosting> jobs);
    ArrayList<JobPosting> getAllJobPostings();

    // Sorting and pagination methods
    ArrayList<JobPosting> sortJobsBySalary();
    ArrayList<JobPosting> paginateJobs(int pageNumber, int pageSize);
}
