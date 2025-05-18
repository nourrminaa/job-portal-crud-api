package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.service.MongoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final DataCache dataCache;
    private final MongoDBService mongoDBService;

    public UserController(DataCache dataCache, MongoDBService mongoDBService) {
        this.dataCache = dataCache;
        this.mongoDBService = mongoDBService;
    }

    // Get all users from cache
    @GetMapping
    public ResponseEntity<ArrayList<User>> getAllUsers() {
        return ResponseEntity.ok(dataCache.getUsers());
    }

    // Create new user and update cache
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        ArrayList<User> users = dataCache.getUsers();
        users.add(user);
        dataCache.setUsers(users);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok(user);
    }

    // Delete all users and rewrite cache
    @DeleteMapping
    public ResponseEntity<Void> deleteAndRewriteUsers(@RequestBody ArrayList<User> newUsers) {
        dataCache.setUsers(newUsers);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        return ResponseEntity.ok().build();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return dataCache.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        ArrayList<User> users = dataCache.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, updatedUser);
                dataCache.setUsers(users);

                // On demand sync with MongoDB
                mongoDBService.saveDataToMongoDB();

                return ResponseEntity.ok(updatedUser);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        ArrayList<User> users = dataCache.getUsers();
        boolean removed = users.removeIf(user -> user.getId().equals(id));

        if (removed) {
            dataCache.setUsers(users);

            // On demand sync with MongoDB
            mongoDBService.saveDataToMongoDB();

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Force refresh from MongoDB
    @GetMapping("/refresh")
    public ResponseEntity<Void> refreshFromMongoDB() {
        mongoDBService.loadDataFromMongoDB();
        return ResponseEntity.ok().build();
    }
}