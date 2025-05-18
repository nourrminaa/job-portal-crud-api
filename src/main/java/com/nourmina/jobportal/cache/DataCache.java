package com.nourmina.jobportal.cache;

import com.nourmina.jobportal.model.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataCache {

    private final List<User> users = new ArrayList<>();
    private final List<JobPosting> jobs = new ArrayList<>();
    private final List<Application> applications = new ArrayList<>();

    private boolean isDirty = false;

    public List<User> getUsers() {
        return users;
    }

    public List<JobPosting> getJobs() {
        return jobs;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void clearDirty() {
        this.isDirty = false;
    }
}
