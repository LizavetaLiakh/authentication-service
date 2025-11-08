package com.innowise.authorization.exception;

public class AuthorizedUserNotFoundException extends RuntimeException {
    public AuthorizedUserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
