// DTOs are simple boxes that hold only the info we want to share when sending or getting data.
// They make sure we don’t share private stuff like passwords by accident.
// Using DTOs helps keep things neat and easy to understand when apps talk to each other.
// For example, if the full user has lots of info, the DTO only sends the important parts like name and email.
// Mainly used for authentication only

package com.nourmina.jobportal.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name must be 2-30 letters only")
    private String fname;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Last name must be 2-30 letters only")
    private String lname;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    private String resume;
    private List<String> skills;
    private String company;
    private String contactInfo;

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
