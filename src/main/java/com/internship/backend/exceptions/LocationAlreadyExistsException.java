package com.internship.backend.exceptions;

public class LocationAlreadyExistsException extends Exception{
    public LocationAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
