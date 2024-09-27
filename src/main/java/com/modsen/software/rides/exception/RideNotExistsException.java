package com.modsen.software.rides.exception;

import org.springframework.http.HttpStatus;

public class RideNotExistsException extends BaseCustomException {
    public RideNotExistsException(String message) {
        super(HttpStatus.NOT_FOUND.value(), message);
    }
}
