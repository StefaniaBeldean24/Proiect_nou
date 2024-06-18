package com.internship.backend.exceptions;

public class ReservationAlreadyExists extends Exception {
    public ReservationAlreadyExists(String errorMessage) {super(errorMessage);}
}
