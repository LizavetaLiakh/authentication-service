package com.innowise.authorization.exception;

public class UserWithUsernameNotFoundException extends RuntimeException {
    public UserWithUsernameNotFoundException(String username) {
        super("User with username " + username + " not found");
    }
}
