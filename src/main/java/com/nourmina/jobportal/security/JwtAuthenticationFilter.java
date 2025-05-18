package com.nourmina.jobportal.security;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nourmina.jobportal.dto.AuthRequest;
import com.nourmina.jobportal.dto.AuthResponse;
import com.nourmina.jobportal.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.*;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        setFilterProcessesUrl("/api/auth/login"); // Custom login URL
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        String token = tokenProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(
                token,
                user.getRole(),
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
    }
}