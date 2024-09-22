package com.modsen.software.rides.exception;

import org.springframework.http.HttpStatus;

public class RideAlreadyExistsException extends BaseCustomException{

    public RideAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
