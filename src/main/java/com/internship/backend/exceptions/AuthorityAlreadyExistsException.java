package com.internship.backend.exceptions;

public class AuthorityAlreadyExistsException extends Exception {
    public AuthorityAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
