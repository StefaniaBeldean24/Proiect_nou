package com.internship.backend.exceptions;

public class PriceIdDoesNotExistException extends Exception{
    public PriceIdDoesNotExistException(String errorMessage){
        super(errorMessage);
    }
}
