package com.innowise.authorization.exception;

public class UserWithEmailNotFoundException extends RuntimeException {
    public UserWithEmailNotFoundException(String email) {
        super("User with e-mail " + email + " not found");
    }
}
