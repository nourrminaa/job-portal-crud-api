package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    ArrayList<User> getAllUsers();
    UserDetails loadUserByUsername(String email);
    void displayAllUsers();
}
