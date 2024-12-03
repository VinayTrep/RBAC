package com.example.vrcSecurityAssignment.exception;

public class UserEmailAlreadyExistsException extends RuntimeException {
    public UserEmailAlreadyExistsException(String message) {
        super(message);
    }
}
