package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.cache.DataCache;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.security.JwtService;
import com.nourmina.jobportal.service.MongoDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final DataCache dataCache;
    private final JwtService jwtService;
    private final MongoDBService mongoDBService;

    public AuthController(DataCache dataCache, JwtService jwtService, MongoDBService mongoDBService) {
        this.dataCache = dataCache;
        this.jwtService = jwtService;
        this.mongoDBService = mongoDBService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Optional<User> user = dataCache.getUsers().stream()
                .filter(u -> u.getEmail().equals(loginRequest.get("email")))
                .findFirst();

        if (user.isPresent() && user.get().getPassword().equals(loginRequest.get("password"))) {
            String token = jwtService.generateToken(user.get());
            return ResponseEntity.ok(Map.of("token", token, "user", user.get()));
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser) {
        if (dataCache.getUsers().stream()
                .anyMatch(u -> u.getEmail().equals(newUser.getEmail()))) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        ArrayList<User> users = dataCache.getUsers();
        users.add(newUser);
        dataCache.setUsers(users);

        // On demand sync with MongoDB
        mongoDBService.saveDataToMongoDB();

        String token = jwtService.generateToken(newUser);
        return ResponseEntity.ok(Map.of("token", token, "user", newUser));
    }
}