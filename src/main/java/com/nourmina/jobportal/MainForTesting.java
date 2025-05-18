package com.nourmina.jobportal;

import java.util.ArrayList;
import java.util.Arrays;

import com.nourmina.jobportal.model.*;

public class MainForTesting {
    public static void main(String[] args) {

        // ------------------ CANDIDATES ------------------

        // Normal candidate
        ArrayList<String> skills1 = new ArrayList<String>();
        skills1.add("Java");
        skills1.add("Spring");
        skills1.add("MongoDB");
        skills1.add("React");
        skills1.add("Node.js");
        skills1.add("Python");

        Candidate candidate1 = new Candidate("Nour", "Mina", "nour@example.com", "password123", "resume.pdf", skills1);
        System.out.println("Candidate 1 created: " + candidate1.getFirstName() + ", Skills: " + candidate1.getSkills());

        // Candidate with no skills
        ArrayList<String> emptySkills = new ArrayList<String>();
        Candidate candidate2 = new Candidate("Sarah", "Lee", "sarah@example.com", "pass456", "cv.pdf", emptySkills);
        System.out.println("Candidate 2 created: " + candidate2.getFirstName() + ", Skills: " + candidate2.getSkills());

        // ------------------ JOB POSTINGS ------------------

        ArrayList<String> jobSkills1 = new ArrayList<String>();
        jobSkills1.add("Java");
        jobSkills1.add("MongoDB");
        jobSkills1.add("Spring");
        jobSkills1.add("REST APIs");

        ArrayList<String> jobSkills2 = new ArrayList<String>();
        jobSkills2.add("React");
        jobSkills2.add("HTML");
        jobSkills2.add("CSS");

        ArrayList<String> jobSkills3 = new ArrayList<String>(); // No required skills

        JobPosting job1 = new JobPosting("Java Developer", "Develop backend", "Beirut", jobSkills1, "recruiter123");
        JobPosting job2 = new JobPosting("Frontend Developer", "Create UI", "Tripoli", jobSkills2, "recruiter456");
        JobPosting job3 = new JobPosting("Content Writer", "Write articles", "Remote", jobSkills3, "recruiter789");

        System.out.println("Job 1: " + job1.getTitle() + " requires " + job1.getRequiredSkills());
        System.out.println("Job 2: " + job2.getTitle() + " requires " + job2.getRequiredSkills());
        System.out.println("Job 3: " + job3.getTitle() + " requires " + job3.getRequiredSkills());

        // ------------------ APPLICATIONS ------------------

        Application app1 = new Application(candidate1.getId(), job1.getId(), Application.STATUS_APPLIED);
        System.out.println("Application 1 status: " + app1.getStatus());

        app1.setStatus(Application.STATUS_SHORTLISTED);
        System.out.println("App1 new status (should be SHORTLISTED): " + app1.getStatus());

        Application app2 = new Application(candidate1.getId(), job2.getId(), Application.STATUS_PENDING);
        System.out.println("App2 status (should be PENDING): " + app2.getStatus());

        Application app3 = new Application(candidate2.getId(), job1.getId(), Application.STATUS_REJECTED);
        System.out.println("App3 status (should be REJECTED): " + app3.getStatus());

        Application app4 = new Application(candidate2.getId(), job2.getId(), Application.STATUS_APPLIED);
        System.out.println("App4 status (candidate2 has no skills): " + app4.getStatus());

        Application app5 = new Application(candidate2.getId(), job3.getId(), Application.STATUS_APPLIED);
        System.out.println("App5 status (job3 has no required skills): " + app5.getStatus());

        Application app6 = new Application(candidate1.getId(), job1.getId(), Application.STATUS_PENDING);
        System.out.println("App6 (duplicate application to job1): " + app6.getStatus());

        // ------------------ SEARCH FUNCTIONALITY ------------------

        ArrayList<JobPosting> allJobs = new ArrayList<JobPosting>();
        allJobs.add(job1);
        allJobs.add(job2);
        allJobs.add(job3);

        int javaCount = 0;
        int devCount = 0;
        int notFoundCount = 0;

        for (int i = 0; i < allJobs.size(); i++) {
            String title = allJobs.get(i).getTitle().toLowerCase();

            if (title.contains("java")) {
                javaCount++;
            }
            if (title.contains("developer")) {
                devCount++;
            }
            if (title.contains("ninja")) {
                notFoundCount++;
            }
        }

        System.out.println("Jobs with 'java' in title: " + javaCount); // expect 1
        System.out.println("Jobs with 'developer' in title: " + devCount); // expect 2
        System.out.println("Jobs with 'ninja' in title: " + notFoundCount); // expect 0

        // ------------------ DISPLAY JOBS ------------------
        System.out.println("\n--- List of All Jobs ---");
        for (int i = 0; i < allJobs.size(); i++) {
            JobPosting job = allJobs.get(i);
            System.out.println("- " + job.getTitle() + " | Location: " + job.getLocation() + " | Skills: " + job.getRequiredSkills());
        }
    }
}
