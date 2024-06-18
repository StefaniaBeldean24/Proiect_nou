package com.internship.backend.exceptions;

public class TennisCourtDoesNotExistsException extends Exception{
    public TennisCourtDoesNotExistsException(String errorMessage){
        super(errorMessage);
    }
}
