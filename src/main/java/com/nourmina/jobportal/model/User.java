package com.nourmina.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Single collection for User, Candidate, Recruiter
@Document(collection = "users")   // Tells Spring this class should be stored as a document in MongoDB
public class User implements UserDetails {

    @Id  // Marks 'id' as the unique identifier for MongoDB document
    private String id;

    private String fname;
    private String lname;

    @Field("email")
    @Indexed(unique = true)  // Mongo uniqueness enforced at DB level
    private String email;     // The user's email address (should be unique)

    private String password;  // The user's password (stored hashed)
    private String role; // Role (e.g., CANDIDATE, RECRUITER)

    // Constructors
    public User() {
        this(null, null, null, null, null);
    }

    public User(String fname, String lname, String email, String password, String role) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public void setPassword(String password) { this.password = password; }
    @Override
    public String getPassword() { return password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String getUsername() {
        return email; // Using email as username for authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Simple implementation: no roles/authorities for now
        return Collections.emptyList(); // or implement real roles later
    }

    // Passowords were excluded on purpose !!!
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Name: " + fname + " " + lname + "\n" +
                "Email: " + email + "\n" +
                "Role: " + role;
    }
}
