package com.nourmina.jobportal.cache;

import com.nourmina.jobportal.model.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class DataCache {
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<JobPosting> jobs = new ArrayList<>();
    private final ArrayList<Application> applications = new ArrayList<>();

    private volatile boolean isDirty = false;

    // Synchronized getters and setters
    public synchronized ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public synchronized void setUsers(ArrayList<User> updatedUsers) {
        users.clear();
        users.addAll(updatedUsers);
        isDirty = true;
    }

    public synchronized ArrayList<JobPosting> getJobs() {
        return new ArrayList<>(jobs);
    }

    public synchronized void setJobs(ArrayList<JobPosting> updatedJobs) {
        jobs.clear();
        jobs.addAll(updatedJobs);
        isDirty = true;
    }

    public synchronized ArrayList<Application> getApplications() {
        return new ArrayList<>(applications);
    }

    public synchronized void setApplications(ArrayList<Application> updatedApplications) {
        applications.clear();
        applications.addAll(updatedApplications);
        isDirty = true;
    }

    public synchronized boolean isDirty() {
        return isDirty;
    }

    public synchronized void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }
}