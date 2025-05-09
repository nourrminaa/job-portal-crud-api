package com.nourmina.jobportal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Mark this class as a REST controller that will handle HTTP requests
@RestController
public class HelloController {

    // Define a method to handle GET requests to the root ("/") URL
    @GetMapping("/")
    public String hello() {
        // Return a simple message that will be sent as the response
        return "Hello from Spring!";
    }
}
