package com.example.ridepal.exceptions;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super("You are not authorized!");
    }
}
