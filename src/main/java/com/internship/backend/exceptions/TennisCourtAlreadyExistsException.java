package com.internship.backend.exceptions;

public class TennisCourtAlreadyExistsException extends Exception {
    public TennisCourtAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
