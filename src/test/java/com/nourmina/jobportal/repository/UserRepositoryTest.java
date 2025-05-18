package com.nourmina.jobportal.repository;

import com.nourmina.jobportal.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=4.0.2")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Clear the repository
        userRepository.deleteAll();

        // Create a test user
        testUser = new User("John", "Doe", "john.doe@example.com", "password123", "CANDIDATE");
        userRepository.save(testUser);
    }

    @Test
    public void shouldFindUserByEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    public void shouldReturnEmptyWhenEmailNotFound() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void shouldCheckIfEmailExists() {
        // When
        boolean exists = userRepository.existsByEmail("john.doe@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}