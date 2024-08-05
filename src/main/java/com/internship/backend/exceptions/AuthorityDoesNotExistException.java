package com.internship.backend.exceptions;

public class AuthorityDoesNotExistException extends Exception {
    public AuthorityDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
