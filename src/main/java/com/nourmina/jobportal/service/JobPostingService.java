package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.JobPosting;
import java.util.ArrayList;

public interface JobPostingService {
    ArrayList<JobPosting> listJobs();
    ArrayList<JobPosting> searchJobs(String keyword);
}
