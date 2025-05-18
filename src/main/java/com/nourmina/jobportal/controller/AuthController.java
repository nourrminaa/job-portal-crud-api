package com.nourmina.jobportal.controller;

import com.nourmina.jobportal.dto.AuthResponse;
import com.nourmina.jobportal.dto.LoginRequest;
import com.nourmina.jobportal.dto.RegisterRequest;
import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        // Convert roles (which might come as List<String>) to ArrayList<String>
        ArrayList<String> roles = new ArrayList<>();
        roles.add(request.getRole()); // example: single role, adjust if multiple

        User user = authService.register(
                request.getFname(),
                request.getLname(),
                request.getEmail(),
                request.getPassword(),
                roles
        );

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());
        // For demo, token is just dummy. Replace with JWT token generation if you want.
        String token = "dummy-jwt-token-for-" + user.getEmail();
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
