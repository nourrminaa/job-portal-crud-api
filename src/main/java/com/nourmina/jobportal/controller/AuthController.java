package com.example.jobportal.controller;

import com.example.jobportal.model.User;
import com.example.jobportal.security.JwtUtil;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            User authenticatedUser = userService.authenticate(email, password);
            String token = jwtUtil.generateToken(authenticatedUser.getId(), authenticatedUser.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("id", authenticatedUser.getId());
            response.put("name", authenticatedUser.getName());
            response.put("email", authenticatedUser.getEmail());
            response.put("role", authenticatedUser.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            String token = jwtUtil.generateToken(createdUser.getId(), createdUser.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("id", createdUser.getId());
            response.put("name", createdUser.getName());
            response.put("email", createdUser.getEmail());
            response.put("role", createdUser.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();

        if (jwtUtil.validateToken(token)) {
            response.put("valid", true);
            response.put("userId", jwtUtil.extractUserId(token));
            response.put("role", jwtUtil.extractRole(token));
        } else {
            response.put("valid", false);
        }

        return ResponseEntity.ok(response);
    }
}