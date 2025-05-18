package com.nourmina.jobportal.service;

import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.validation.UserValidator;
import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.cache.DataCache;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final DataCache dataCache;

    public UserServiceImpl(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        ArrayList<User> users = dataCache.getUsers();
        for (User user : users) {
            if (email.equalsIgnoreCase(user.getEmail())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        ArrayList<User> users = dataCache.getUsers();
        for (User user : users) {
            if (id.equals(user.getId())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public ArrayList<User> getAllUsers() {
        // Return a new list to avoid external modifications to internal cache
        return new ArrayList<>(dataCache.getUsers());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Assuming your User implements UserDetails or adapt as needed
        return user;
    }

    public void displayAllUsers() {
        ArrayList<User> users= getAllUsers();

        if (users == null || users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.println("Users List:");
        for (User user : users) {
            System.out.println(user); // Calls user.toString()
            System.out.println("-----------------------");
        }
    }
}
