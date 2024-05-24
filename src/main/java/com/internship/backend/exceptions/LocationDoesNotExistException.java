package com.internship.backend.exceptions;

public class LocationDoesNotExistException extends Exception{
    public LocationDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
