package com.nourmina.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class JobPortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }
}