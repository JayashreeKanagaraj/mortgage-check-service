package com.ing.mortgagecheckservice.exception;

public class InterestRateNotFoundException extends RuntimeException{
    public InterestRateNotFoundException(String message) {
        super(message);
    }
}
