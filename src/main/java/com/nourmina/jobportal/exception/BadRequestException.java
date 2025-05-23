package com.nourmina.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HTTP Status Codes:
//  - 200 -> success
//  - 400 -> bad request (invalid input)
//  - 404 -> item not found
//  - 401 -> not logged in (unauthorized access)
//  - 403 -> forbidden
//  - 500 -> server crashed

@ResponseStatus(HttpStatus.BAD_REQUEST) // tells spring to return 400 when this excetion is thrown
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L; // for serialization

    // contstructor -> takes the error message and passes it to the parent class RuntimeException
    public BadRequestException(String message) {
        super(message);
    }
}