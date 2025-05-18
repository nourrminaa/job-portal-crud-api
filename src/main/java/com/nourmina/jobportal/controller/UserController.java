package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final DataCache dataCache;

    public UserController(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    @GetMapping
    public ResponseEntity<ArrayList<User>> getAllUsers() {
        return ResponseEntity.ok(dataCache.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = dataCache.getUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        ArrayList<User> users = dataCache.getUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, updatedUser);
                dataCache.setUsers(users);
                return ResponseEntity.ok(updatedUser);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        ArrayList<User> users = dataCache.getUsers();
        if (users.removeIf(user -> user.getId().equals(id))) {
            dataCache.setUsers(users);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}