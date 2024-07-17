package com.internship.backend.exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
