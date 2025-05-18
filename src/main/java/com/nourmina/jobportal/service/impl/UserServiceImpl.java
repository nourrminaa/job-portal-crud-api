package com.nourmina.jobportal.service.impl;

import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.Candidate;
import com.nourmina.jobportal.model.Recruiter;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.service.UserService;
import com.nourmina.jobportal.validation.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final ArrayList<User> users = new ArrayList<>();

    @Override
    public User registerUser(User user) {
        UserValidator.validate(user);

        boolean emailExists = users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));
        if (emailExists) {
            throw new BadRequestException("Email already registered");
        }
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<User> findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public User updateUser(String id, User updatedUser) {
        UserValidator.validate(updatedUser);

        User existingUser = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword()); // You might want to encrypt password here

        return existingUser;
    }

    public ArrayList<User> getAllUsers() {
        return users;
    }

    @Override
    public void deleteUser(String id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
