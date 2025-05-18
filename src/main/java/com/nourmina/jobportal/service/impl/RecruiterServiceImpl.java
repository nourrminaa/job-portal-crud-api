package com.nourmina.jobportal.service.impl;

import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.service.RecruiterService;
import com.nourmina.jobportal.validation.JobPostingValidator;
import com.nourmina.jobportal.validation.RecruiterValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final ArrayList<Recruiter> recruiters = new ArrayList<>();
    private final ArrayList<JobPosting> jobPostings;

    public RecruiterServiceImpl(ArrayList<JobPosting> jobPostings) {
        this.jobPostings = jobPostings;
    }

    @Override
    public Recruiter registerRecruiter(Recruiter recruiter) {
        RecruiterValidator.validate(recruiter);
        // Optionally check for duplicate email here

        recruiters.add(recruiter);
        return recruiter;
    }

    @Override
    public Optional<Recruiter> findById(String id) {
        return recruiters.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    @Override
    public JobPosting createJob(String recruiterId, JobPosting jobPosting) {
        Recruiter recruiter = findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        JobPostingValidator.validate(jobPosting);

        jobPosting.setRecruiterId(recruiter.getId());
        jobPostings.add(jobPosting);
        return jobPosting;
    }

    @Override
    public JobPosting updateJob(String jobId, JobPosting updatedJob) {
        JobPostingValidator.validate(updatedJob);

        for (JobPosting job : jobPostings) {
            if (job.getId().equals(jobId)) {
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setLocation(updatedJob.getLocation());
                job.setSalary(updatedJob.getSalary());
                return job;
            }
        }
        throw new ResourceNotFoundException("Job not found");
    }

    @Override
    public void deleteJob(String jobId) {
        boolean removed = jobPostings.removeIf(job -> job.getId().equals(jobId));
        if (!removed) {
            throw new ResourceNotFoundException("Job not found");
        }
    }

    public ArrayList<Recruiter> getAllRecruiters() {
        return recruiters;
    }

    public ArrayList<Recruiter> filterRecruitersByKeyword(String keyword) {
        ArrayList<Recruiter> filtered = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Recruiter recruiter : recruiters) {
            boolean matchesCompany = recruiter.getCompany() != null && recruiter.getCompany().toLowerCase().contains(lowerKeyword);
            boolean matchesName = false;

            if (recruiter.getFirstName() != null && recruiter.getLastName() != null) {
                String fullName = (recruiter.getFirstName() + " " + recruiter.getLastName()).toLowerCase();
                matchesName = fullName.contains(lowerKeyword);
            }

            if (matchesCompany || matchesName) {
                filtered.add(recruiter);
            }
        }
        return filtered;
    }

}
