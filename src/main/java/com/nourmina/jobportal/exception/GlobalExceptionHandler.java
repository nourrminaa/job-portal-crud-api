package com.nourmina.exception;

import com.nourmina.jobportal.payload.ApiResponse; // the custom api reponse (succes/ error + message)

import com.nourmina.jobportal.exception.BadRequestException;
import com.nourmina.jobportal.exception.ResourceNotFoundException;
import com.nourmina.jobportal.exception.ForbiddenException;
import com.nourmina.jobportal.exception.UnauthorizedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// catches any exception in the controller and returns the error message customly made
@RestControllerAdvice // global exception handler for REST controllers
                    // when using this line, i don't need to recall this class anywhere this is why it is 'global'
public class GlobalExceptionHandler {

    // not found (http 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // invalid input (http 400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // unauthorized access (http 401)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // access denial (logged in but not allowed (http 403))
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse> handleForbidden(ForbiddenException ex) {
        return new ResponseEntity<>(new ApiResponse(false, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    // catches all other unhandled exceptions (default)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAllOtherExceptions(Exception ex) {
        return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred."), HttpStatus.INTERNAL_SERVER_ERROR); // http 500
    }
}
