package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.Application;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSyncService {

    private final MongoTemplate mongoTemplate;
    private final DataCache dataCache;

    @Autowired
    public DataSyncService(MongoTemplate mongoTemplate, DataCache dataCache) {
        this.mongoTemplate = mongoTemplate;
        this.dataCache = dataCache;
    }

    @PostConstruct
    public void init() {
        loadAllDataFromMongoDB();
    }

    public void loadAllDataFromMongoDB() {
        try {
            // Load users from MongoDB
            List<User> users = mongoTemplate.findAll(User.class);
            dataCache.setUsers(new ArrayList<>(users));

            // Load job postings from MongoDB
            List<JobPosting> jobs = mongoTemplate.findAll(JobPosting.class);
            dataCache.setJobs(new ArrayList<>(jobs));

            // Load applications from MongoDB
            List<Application> applications = mongoTemplate.findAll(Application.class);
            dataCache.setApplications(new ArrayList<>(applications));

            // Reset the dirty flag after successful load
            dataCache.setDirty(false);

            System.out.println("Data loaded from MongoDB: " +
                    users.size() + " users, " +
                    jobs.size() + " jobs, " +
                    applications.size() + " applications");
        } catch (Exception e) {
            System.err.println("Error loading data from MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAllDataToMongoDB() {
        try {
            if (dataCache.isDirty()) {
                // Save all users
                mongoTemplate.dropCollection(User.class);
                for (User user : dataCache.getUsers()) {
                    mongoTemplate.save(user);
                }

                // Save all job postings
                mongoTemplate.dropCollection(JobPosting.class);
                for (JobPosting job : dataCache.getJobs()) {
                    mongoTemplate.save(job);
                }

                // Save all applications
                mongoTemplate.dropCollection(Application.class);
                for (Application application : dataCache.getApplications()) {
                    mongoTemplate.save(application);
                }

                // Reset the dirty flag after successful save
                dataCache.setDirty(false);

                System.out.println("Data saved to MongoDB: " +
                        dataCache.getUsers().size() + " users, " +
                        dataCache.getJobs().size() + " jobs, " +
                        dataCache.getApplications().size() + " applications");
            }
        } catch (Exception e) {
            System.err.println("Error saving data to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void scheduledSync() {
        if (dataCache.isDirty()) {
            saveAllDataToMongoDB();
        }
    }
}