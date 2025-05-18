package com.nourmina.jobportal.service.impl;

import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.service.JobPostingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;

@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final ArrayList<JobPosting> jobPostings = new ArrayList<>();

    @Override
    public ArrayList<JobPosting> listJobs() {
        return new ArrayList<>(jobPostings);
    }

    @Override
    public ArrayList<JobPosting> searchJobs(String keyword) {
        ArrayList<JobPosting> result = new ArrayList<>();
        for (JobPosting job : jobPostings) {
            if (job.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(job);
            }
        }
        return result;
    }

    @Override
    public void loadJobPostings(ArrayList<JobPosting> jobs) {
        jobPostings.clear();
        jobPostings.addAll(jobs);
    }

    @Override
    public ArrayList<JobPosting> getAllJobPostings() {
        return jobPostings;
    }

    // Simple beginner-friendly sorting method: sort by salary ascending
    public ArrayList<JobPosting> sortJobsBySalary() {
        ArrayList<JobPosting> sorted = new ArrayList<>(jobPostings);
        sorted.sort(Comparator.comparingDouble(JobPosting::getSalary));
        return sorted;
    }

    // Simple beginner-friendly pagination method for job postings
    // pageNumber starts at 1
    public ArrayList<JobPosting> paginateJobs(int pageNumber, int pageSize) {
        ArrayList<JobPosting> paginated = new ArrayList<>();
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, jobPostings.size());

        if (start >= jobPostings.size() || start < 0) {
            return paginated; // empty list for invalid page
        }

        for (int i = start; i < end; i++) {
            paginated.add(jobPostings.get(i));
        }
        return paginated;
    }
}
