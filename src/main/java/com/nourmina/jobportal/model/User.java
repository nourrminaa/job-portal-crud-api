package com.nourmina.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Single collection for User, Candidate, Recruiter
@Document(collection = "users")   // Tells Spring this class should be stored as a document in MongoDB
public class User implements UserDetails {

    @Id  // Marks 'id' as the unique identifier for MongoDB document
    private String id;        // MongoDB will generate this automatically if left null

    private String fname;
    private String lname;

    @Field("email")
    @Indexed(unique = true)  // Mongo uniqueness enforced at DB level
    private String email;     // The user's email address (should be unique)

    private String password;  // The user's password (stored hashed)
    ArrayList<String> roles; // List of roles (e.g., ROLE_CANDIDATE, ROLE_RECRUITER)

    // Constructors
    public User() {
        this(null, null, null, null, new ArrayList<>());
    }

    public User(String fname, String lname, String email, String password, ArrayList<String> roles) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.roles = roles != null ? roles : new ArrayList<>();
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

    public ArrayList<String> getRoles() { return roles; }
    public void setRoles(ArrayList<String> roles) { this.roles = roles; }

    @Override
    public String getUsername() {
        return email; // Using email as username for authentication
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
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
}
