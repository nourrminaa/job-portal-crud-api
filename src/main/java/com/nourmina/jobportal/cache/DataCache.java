package com.nourmina.jobportal.cache;

import com.nourmina.jobportal.model.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DataCache {

    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<JobPosting> jobs = new ArrayList<>();
    private final ArrayList<Application> applications = new ArrayList<>();

    private boolean isDirty = false;

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<JobPosting> getJobs() {
        return jobs;
    }

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public void setUsers(ArrayList<User> updatedUsers) {
        users.clear();
        users.addAll(updatedUsers);
        isDirty = true;
    }

    public void setJobs(ArrayList<JobPosting> updatedJobs) {
        jobs.clear();
        jobs.addAll(updatedJobs);
        isDirty = true;
    }

    public void setApplications(ArrayList<Application> updatedApplications) {
        applications.clear();
        applications.addAll(updatedApplications);
        isDirty = true;
    }

}
