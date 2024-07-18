package com.internship.backend.exceptions;

public class IdUserNotFoundException extends Exception{
    public IdUserNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
