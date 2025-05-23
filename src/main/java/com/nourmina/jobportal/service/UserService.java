package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.exception.UnauthorizedException;

import com.nourmina.jobportal.model.User;

import com.nourmina.jobportal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // tells spring this is a service class
public class UserService {

    private UserRepository userRepository; // interface to talk to mongodb for User data

    private PasswordEncoder passwordEncoder; // to hash passwords before saving

    // Create a new user
    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user); // save to db
    }

    // Get user by ID
    public User getUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        return optionalUser.get();
    }

    // Get user by email
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }

    // Update user
    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        User existingUser = optionalUser.get();

        // Update email
        existingUser.setEmail(updatedUser.getEmail());

        // Update based on role
        String role = existingUser.getRole();

        if (role != null && role.equals("RECRUITER")) {
            existingUser.setCompany(updatedUser.getCompany());
        } else if (role != null && role.equals("CANDIDATE")) {
            existingUser.setSkills(updatedUser.getSkills());
            existingUser.setResume(updatedUser.getResume());
        }

        // Update password if present
        String newPassword = updatedUser.getPassword();
        if (newPassword != null && !newPassword.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(existingUser);
    }

    // Delete user
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // Get users by role
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    // Authenticate user (for login)
    public User authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return user;
    }
}
