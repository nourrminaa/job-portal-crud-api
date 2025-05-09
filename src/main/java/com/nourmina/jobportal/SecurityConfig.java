
package com.nourmina.jobportal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Only for usage when testing to simplify the testing process
// Mark this class as a configuration class that contains Spring beans
@Configuration
public class SecurityConfig {

    // Define a bean to configure HTTP security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth   // Authorize requests based on the defined rules
                        .anyRequest().permitAll()   // Allow all requests without authentication
                )
                .csrf(csrf -> csrf.disable())         // Disable CSRF (Cross-Site Request Forgery) protection
                .formLogin(form -> form.disable())    // Disable default form-based login
                .httpBasic(httpBasic -> httpBasic.disable()); // Disable HTTP Basic Authentication

        // Build and return the security configuration
        return http.build();
    }
}
