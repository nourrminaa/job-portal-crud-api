package com.nourmina.jobportal.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class MongoConfig {

    private static final String MONGODB_URI = "mongodb+srv://nourmina998:YXmiJ1aIiBbRvWUx@nourcluster.wmdrk7q.mongodb.net/jobportal?retryWrites=true&w=majority&appName=NourCluster";

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(MONGODB_URI);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "jobportal");
    }
}