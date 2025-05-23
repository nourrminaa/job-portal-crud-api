package com.nourmina.jobportal.security;

import com.nourmina.jobportal.security.JwtAuthenticationFilter;
import com.nourmina.jobportal.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity // tells Spring this class is for security configuration
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter, UserService userService) {
        this.jwtFilter = jwtFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF disabled: because we are using JWT (not sessions)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT is stateless, we don't store sessions in backend
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // allow all users (even without a token) to access anything that starts with /auth/ (login and register)
                        .anyRequest().authenticated() // all other endpoints must include a valid JWT token in the request
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // before checking username+password, check if there's a valid JWT token in the header
                .build(); // returns the full security setup to Spring
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // allows Spring to handle user authentication using UserDetailsService (like UserService)
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // hashes the password securely using BCrypt
    }
}
