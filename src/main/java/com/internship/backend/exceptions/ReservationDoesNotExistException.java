package com.internship.backend.exceptions;

public class ReservationDoesNotExistException extends Exception{
    public ReservationDoesNotExistException(String errorMessage){
        super(errorMessage);
    }
}
