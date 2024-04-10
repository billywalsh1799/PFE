package com.example.jwttest.exceptionHandling.exceptions;

public class UsedEmailException extends RuntimeException {
    public UsedEmailException(String message){
        super(message);
    }
    
}
