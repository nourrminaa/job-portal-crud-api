package com.nourmina.jobportal.service;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;

@Service
public class DataSyncService {

    private final DataCache dataCache;
    private final MongoTemplate mongoTemplate;

    public DataSyncService(DataCache dataCache, MongoTemplate mongoTemplate) {
        this.dataCache = dataCache;
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void loadInitialData() {
        // Load data from MongoDB to cache on startup
        dataCache.setUsers(new ArrayList<>(mongoTemplate.findAll(User.class, "users")));
        dataCache.setJobs(new ArrayList<>(mongoTemplate.findAll(JobPosting.class, "jobs")));
        dataCache.setApplications(new ArrayList<>(mongoTemplate.findAll(Application.class, "applications")));
    }

    @Scheduled(fixedRate = 300000) // Sync every 5 minutes
    public void syncToMongoDB() {
        // Sync all data to MongoDB
        mongoTemplate.dropCollection("users");
        mongoTemplate.insertAll(new ArrayList<>(dataCache.getUsers()));

        mongoTemplate.dropCollection("jobs");
        mongoTemplate.insertAll(new ArrayList<>(dataCache.getJobs()));

        mongoTemplate.dropCollection("applications");
        mongoTemplate.insertAll(new ArrayList<>(dataCache.getApplications()));
    }
}