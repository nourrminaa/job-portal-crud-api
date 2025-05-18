package com.nourmina.jobportal.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nourmina.jobportal.JobPortalApplication;
import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.JobPosting;
import com.nourmina.jobportal.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A standalone controller tester main class for testing the JobPortal API endpoints.
 * This allows you to test API endpoints without using an external tool like Postman.
 */
@SpringBootApplication
public class ControllerTester {

    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Start the Spring application in a separate thread
        Thread appThread = new Thread(() -> {
            SpringApplication.run(JobPortalApplication.class, args);
        });
        appThread.setDaemon(true);
        appThread.start();

        // Wait a bit for the application to start
        try {
            System.out.println("Starting JobPortal application...");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    testGetAllUsers();
                    break;
                case 2:
                    testGetUserById();
                    break;
                case 3:
                    testCreateUser();
                    break;
                case 4:
                    testGetAllJobs();
                    break;
                case 5:
                    testGetJobById();
                    break;
                case 6:
                    testCreateJob();
                    break;
                case 7:
                    testGetApplications();
                    break;
                case 8:
                    testCreateApplication();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }

        scanner.close();
        System.exit(0);
    }

    private static void printMenu() {
        System.out.println("\n===== JobPortal API Test Menu =====");
        System.out.println("1. Test Get All Users");
        System.out.println("2. Test Get User by ID");
        System.out.println("3. Test Create User");
        System.out.println("4. Test Get All Jobs");
        System.out.println("5. Test Get Job by ID");
        System.out.println("6. Test Create Job");
        System.out.println("7. Test Get Applications");
        System.out.println("8. Test Create Application");
        System.out.println("0. Exit");
        System.out.println("================================");
    }

    private static void testGetAllUsers() {
        try {
            System.out.println("Testing GET /api/users...");
            ResponseEntity<User[]> response = restTemplate.getForEntity(BASE_URL + "/api/users", User[].class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response: ");
            User[] users = response.getBody();
            if (users != null && users.length > 0) {
                for (User user : users) {
                    System.out.println(user);
                }
            } else {
                System.out.println("No users found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testGetUserById() {
        try {
            String userId = getStringInput("Enter user ID: ");
            System.out.println("Testing GET /api/users/" + userId + "...");
            ResponseEntity<User> response = restTemplate.getForEntity(BASE_URL + "/api/users/" + userId, User.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testCreateUser() {
        try {
            System.out.println("Testing POST /api/users...");
            Map<String, Object> user = new HashMap<>();
            user.put("name", getStringInput("Enter name: "));
            user.put("email", getStringInput("Enter email: "));
            user.put("password", getStringInput("Enter password: "));
            user.put("role", getStringInput("Enter role (EMPLOYER/JOBSEEKER): "));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
            ResponseEntity<User> response = restTemplate.postForEntity(BASE_URL + "/api/users", request, User.class);

            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Created user: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testGetAllJobs() {
        try {
            System.out.println("Testing GET /api/jobs...");
            ResponseEntity<JobPosting[]> response = restTemplate.getForEntity(BASE_URL + "/api/jobs", JobPosting[].class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response: ");
            JobPosting[] jobs = response.getBody();
            if (jobs != null && jobs.length > 0) {
                for (JobPosting job : jobs) {
                    System.out.println(job);
                }
            } else {
                System.out.println("No jobs found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testGetJobById() {
        try {
            String jobId = getStringInput("Enter job ID: ");
            System.out.println("Testing GET /api/jobs/" + jobId + "...");
            ResponseEntity<JobPosting> response = restTemplate.getForEntity(BASE_URL + "/api/jobs/" + jobId, JobPosting.class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testCreateJob() {
        try {
            System.out.println("Testing POST /api/jobs...");
            Map<String, Object> job = new HashMap<>();
            job.put("title", getStringInput("Enter job title: "));
            job.put("company", getStringInput("Enter company: "));
            job.put("description", getStringInput("Enter description: "));
            job.put("location", getStringInput("Enter location: "));
            job.put("salary", getDoubleInput("Enter salary: "));
            job.put("employerId", getStringInput("Enter employer ID: "));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(job, headers);
            ResponseEntity<JobPosting> response = restTemplate.postForEntity(BASE_URL + "/api/jobs", request, JobPosting.class);

            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Created job: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testGetApplications() {
        try {
            System.out.println("Testing GET /api/applications...");
            ResponseEntity<Object[]> response = restTemplate.getForEntity(BASE_URL + "/api/applications", Object[].class);
            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response: ");
            Object[] applications = response.getBody();
            if (applications != null && applications.length > 0) {
                for (Object app : applications) {
                    System.out.println(app);
                }
            } else {
                System.out.println("No applications found!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testCreateApplication() {
        try {
            System.out.println("Testing POST /api/applications...");
            Map<String, Object> application = new HashMap<>();
            application.put("jobId", getStringInput("Enter job ID: "));
            application.put("userId", getStringInput("Enter user ID: "));
            application.put("coverLetter", getStringInput("Enter cover letter: "));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(application, headers);
            ResponseEntity<Object> response = restTemplate.postForEntity(BASE_URL + "/api/applications", request, Object.class);

            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Created application: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Helper methods for input
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * This bean will print the data cache contents after application startup
     * It's helpful for debugging data loading issues
     */
    @Bean
    @Profile("!test")
    public CommandLineRunner testDataLoading(DataCache dataCache) {
        return args -> {
            System.out.println("\n======= Initial Data Cache Contents =======");
            System.out.println("Users: " + dataCache.getUsers().size());
            System.out.println("Jobs: " + dataCache.getJobs().size());
            System.out.println("Applications: " + dataCache.getApplications().size());
            System.out.println("==========================================\n");
        };
    }
}