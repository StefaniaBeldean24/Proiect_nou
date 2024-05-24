package com.internship.backend.exceptions;

public class UserDoesNotExistException extends Exception{
    public UserDoesNotExistException(String errorMessage){
        super(errorMessage);
    }
}
