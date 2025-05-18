package com.nourmina.jobportal.service;

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String fname, String lname, String email, String password,
                         ArrayList<String> roles) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(fname, lname, email, hashedPassword, roles);
        return userService.registerUser(user);
    }

    public User login(String email, String rawPassword) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return user;
    }
}
