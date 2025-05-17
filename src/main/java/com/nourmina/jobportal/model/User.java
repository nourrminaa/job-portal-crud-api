package com.nourmina.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document  // Tells Spring this class should be stored as a document in MongoDB
public abstract class User {

    @Id  // Marks 'id' as the unique identifier for MongoDB document
    private String id;        // MongoDB will generate this automatically if left null

    private String fname;
    private String lname;
    private String email;     // The user's email address (should be unique)
    private String password;  // The user's password (should be stored securely)

    // Constructors
    public User() {
        this(null, null, null, null);
    }

    public User(String fname, String lname, String email, String password) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    // Getter and setter methods
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return fname; }
    public void setFirstName(String fname) { this.fname = fname; }

    public String getLastName() { return lname; }
    public void setLastName(String lname) { this.lname = lname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
