package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.ArrayList;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    User updateUser(String id, User updatedUser);
    void deleteUser(String id);
    ArrayList<User> getAllUsers();

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
