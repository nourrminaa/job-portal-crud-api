package com.nourmina.jobportal.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "users") // Marks this class as a MongoDB document
public class Recruiter extends User {

    private String company;        // Company name the recruiter works for
    private String contactInfo;    // Recruiter's contact information

    // Constructors
    public Recruiter() {
        super();  // Call User no-arg constructor
        this.company = null;
        this.contactInfo = null;
    }

    public Recruiter(String fname, String lname, String email, String password, String company, String contactInfo) {
        super(fname, lname, email, password, new ArrayList<>());
        this.roles.add("ROLE_RECRUITER");
        this.company = company;
        this.contactInfo = contactInfo;
    }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
