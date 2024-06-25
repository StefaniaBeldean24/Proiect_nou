package com.internship.backend.exceptions;

import com.internship.backend.model.TennisCourt;

public class TennisCourtAlreadyExistsException extends Exception{
    public TennisCourtAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
