package com.nourmina.jobportal.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "testSecretKeyWithAtLeast32Characters123456789012");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 3600000); // 1 hour
    }

    @Test
    public void shouldGenerateAndValidateToken() {
        // Given
        UserDetails userDetails = new User("test@example.com", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CANDIDATE")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // When
        String token = jwtTokenProvider.generateToken(authentication);

        // Then
        assertThat(token).isNotEmpty();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.getUsernameFromJWT(token)).isEqualTo("test@example.com");
    }

    @Test
    public void shouldExtractAuthoritiesFromToken() {
        // Given
        UserDetails userDetails = new User("test@example.com", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CANDIDATE")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // When
        String token = jwtTokenProvider.generateToken(authentication);

        // Then
        assertThat(jwtTokenProvider.getAuthoritiesFromJWT(token))
                .extracting("authority")
                .containsExactly("ROLE_CANDIDATE");
    }
}