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
public class MongoDBService {
    private final MongoTemplate mongoTemplate;
    private final DataCache dataCache;

    @Autowired
    public MongoDBService(MongoTemplate mongoTemplate, DataCache dataCache) {
        this.mongoTemplate = mongoTemplate;
        this.dataCache = dataCache;
    }

    // Improved save method with error handling and transaction
    @Transactional
    public void saveDataToMongoDB() {
        if (!dataCache.isDirty()) {
            return;
        }

        try {
            // Save users
            saveCollection(User.class, dataCache.getUsers());

            // Save jobs
            saveCollection(JobPosting.class, dataCache.getJobs());

            // Save applications
            saveCollection(Application.class, dataCache.getApplications());

            dataCache.setDirty(false);

            log.info("Successfully saved data to MongoDB: {} users, {} jobs, {} applications",
                    dataCache.getUsers().size(),
                    dataCache.getJobs().size(),
                    dataCache.getApplications().size());
        } catch (Exception e) {
            log.error("Failed to save data to MongoDB", e);
            throw new RuntimeException("Failed to save data to MongoDB", e);
        }
    }

    private <T> void saveCollection(Class<T> entityClass, ArrayList<T> items) {
        mongoTemplate.dropCollection(entityClass);
        if (!items.isEmpty()) {
            mongoTemplate.insertAll(items);
        }
    }

    // Improved load method with error handling
    @Transactional(readOnly = true)
    public void loadDataFromMongoDB() {
        try {
            ArrayList<User> users = new ArrayList<>(mongoTemplate.findAll(User.class));
            ArrayList<JobPosting> jobs = new ArrayList<>(mongoTemplate.findAll(JobPosting.class));
            ArrayList<Application> applications = new ArrayList<>(mongoTemplate.findAll(Application.class));

            dataCache.setUsers(users);
            dataCache.setJobs(jobs);
            dataCache.setApplications(applications);
            dataCache.setDirty(false);

            log.info("Successfully loaded data from MongoDB: {} users, {} jobs, {} applications",
                    users.size(), jobs.size(), applications.size());
        } catch (Exception e) {
            log.error("Failed to load data from MongoDB", e);
            throw new RuntimeException("Failed to load data from MongoDB", e);
        }
    }
}