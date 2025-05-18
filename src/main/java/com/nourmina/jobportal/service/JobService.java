/*package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JobService {
    private final DataCache cache;

    public JobService(DataCache cache) {
        this.cache = cache;
    }

    public ArrayList<JobPosting> getAllJobs() {
        return cache.getJobs();
    }

    public JobPosting getJobById(String id) {
        ArrayList<JobPosting> jobs = cache.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getId().equals(id)) {
                return jobs.get(i);
            }
        }
        throw new IllegalArgumentException("Job not found");
    }

    public ArrayList<JobPosting> getJobsByRecruiter(String recruiterId) {
        ArrayList<JobPosting> result = new ArrayList<>();
        ArrayList<JobPosting> jobs = cache.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getRecruiterId().equals(recruiterId)) {
                result.add(jobs.get(i));
            }
        }
        return result;
    }

    public JobPosting createJob(JobPosting job) {
        cache.getJobs().add(job);
        cache.markDirty();
        return job;
    }

    public JobPosting updateJob(String id, JobPosting updatedJob) {
        ArrayList<JobPosting> jobs = cache.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getId().equals(id)) {
                updatedJob.setId(id);
                jobs.set(i, updatedJob);
                cache.markDirty();
                return updatedJob;
            }
        }
        throw new IllegalArgumentException("Job not found");
    }

    public void deleteJob(String id) {
        ArrayList<JobPosting> jobs = cache.getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getId().equals(id)) {
                jobs.remove(i);
                cache.markDirty();
                return;
            }
        }
        throw new IllegalArgumentException("Job not found");
    }
}*/
