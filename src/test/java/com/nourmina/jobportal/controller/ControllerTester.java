package com.nourmina.jobportal.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("apitest")
public class ControllerTester implements CommandLineRunner {
    private final String BASE = "http://localhost:8080";
    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private String recruiterToken;
    private String candidateToken;
    private String jobId;

    public ControllerTester() {
        System.out.println("ControllerTester bean initialized...................");
    }

    @Override
    public void run(String... args) {
        System.out.println("Starting live API sanity check...\n");

        try {
            registerUsers();
            loginUsers();
            postJob();
            listJobs();
            applyToJob();

            System.out.println("\nAll endpoints responded correctly. Everything looks good!");
        } catch (Exception e) {
            System.out.println("\nSomething went wrong during testing:");
            e.printStackTrace();
        }
    }

    private void registerUsers() {
        System.out.println("→ Step 1: Registering test users...");

        register("Test Recruiter", "recruiter@example.com", "RECRUITER");
        register("Test Candidate", "candidate@example.com", "CANDIDATE");
    }

    private void register(String name, String email, String role) {
        String url = BASE + "/auth/register";
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", "password123");
        body.put("role", role);

        try {
            ResponseEntity<String> response = rest.postForEntity(url, body, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("✔ " + role + " registered: " + email);
            } else {
                System.out.println("⚠ Unexpected response while registering " + email + ": " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠ Possibly already registered: " + email);
        }
    }

    private void loginUsers() throws Exception {
        System.out.println("\n→ Step 2: Logging in users...");

        recruiterToken = login("recruiter@example.com");
        candidateToken = login("candidate@example.com");
    }

    private String login(String email) throws Exception {
        String url = BASE + "/auth/login";
        Map<String, String> login = new HashMap<>();
        login.put("email", email);
        login.put("password", "password123");

        ResponseEntity<String> res = rest.postForEntity(url, login, String.class);
        JsonNode json = mapper.readTree(res.getBody());

        if (json.has("token")) {
            System.out.println("✔ Logged in: " + email);
            return json.get("token").asText();
        } else {
            throw new Exception("✘ Login failed for " + email);
        }
    }

    private void postJob() throws Exception {
        System.out.println("\n→ Step 3: Posting a new job as recruiter...");

        String url = BASE + "/api/jobs";
        Map<String, Object> job = new HashMap<>();
        job.put("title", "API Test Developer");
        job.put("company", "API Inc.");
        job.put("location", "Remote");
        job.put("jobType", "Full-time");
        job.put("salary", 75000.0);
        job.put("description", "Write beautiful APIs.");
        job.put("requiredSkills", "Java, Spring Boot");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(recruiterToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(job, headers);
        ResponseEntity<String> res = rest.postForEntity(url, request, String.class);

        if (res.getStatusCode() == HttpStatus.CREATED) {
            JsonNode json = mapper.readTree(res.getBody());
            jobId = json.get("id").asText();
            System.out.println("✔ Job created successfully (ID: " + jobId + ")");
        } else {
            throw new Exception("✘ Failed to post job");
        }
    }

    private void listJobs() {
        System.out.println("\n→ Step 4: Checking if job listings are showing up...");

        String url = BASE + "/api/jobs";
        ResponseEntity<String> res = rest.getForEntity(url, String.class);

        if (res.getStatusCode() == HttpStatus.OK) {
            System.out.println("✔ Job list fetched — looks good!");
        } else {
            System.out.println("✘ Could not retrieve jobs. Status: " + res.getStatusCode());
        }
    }

    private void applyToJob() throws Exception {
        System.out.println("\n→ Step 5: Candidate is applying to the job...");

        String url = BASE + "/api/applications";
        Map<String, String> body = new HashMap<>();
        body.put("jobId", jobId);
        body.put("coverLetter", "I love APIs and I'm eager to contribute!");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(candidateToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> res = rest.postForEntity(url, request, String.class);

        if (res.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("✔ Application submitted successfully.");
        } else {
            throw new Exception("✘ Application failed. Status: " + res.getStatusCode());
        }
    }
}
