package com.internship.backend.exceptions;

public class InvalidDateException extends Exception{
    public InvalidDateException(String errorMessage){
        super(errorMessage);
    }
}
