package com.example.jwttest.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.jwttest.exceptionHandling.exceptions.ErrorResponse;
import com.example.jwttest.exceptionHandling.exceptions.TokenExpiredException;
import com.example.jwttest.exceptionHandling.exceptions.UsedEmailException;
import com.example.jwttest.exceptionHandling.exceptions.UserAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ErrorResponse(ex.getMessage(),401));
    }

    @ExceptionHandler(UsedEmailException.class)
    public ResponseEntity<ErrorResponse> handleUsedEmailException(UsedEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponse(ex.getMessage(), 409));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ErrorResponse(ex.getMessage(),409));
    }


    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ErrorResponse(ex.getMessage(),401));
    }
}


