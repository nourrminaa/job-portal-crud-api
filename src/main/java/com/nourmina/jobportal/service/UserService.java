package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User createUser(User user) {
        // Check if user with email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + user.getEmail());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);

        // Update fields
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());

        // Only update password if it's provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
