package com.nourmina.jobportal.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document  // Indicates this class will be saved as a document in a MongoDB collection
public class Candidate extends User {

    private String resume;  // Path or content of the candidate's resume
    private ArrayList<String> skills;  // List of skills the candidate has

    // Constructors
    public Candidate() {
        this.skills = new ArrayList<>();  // Use diamond operator for generics
    }

    public Candidate(String fname, String lname, String email, String password, String resume, ArrayList<String> skills) {
        super(fname, lname, email, password);
        this.resume = resume;
        this.skills = skills;
    }

    // Getter and setter methods
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public ArrayList<String> getSkills() { return skills; }
    public void setSkills(ArrayList<String> skills) { this.skills = skills; }
}
