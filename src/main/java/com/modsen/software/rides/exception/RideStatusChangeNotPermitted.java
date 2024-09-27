package com.modsen.software.rides.exception;

import org.springframework.http.HttpStatus;

public class RideStatusChangeNotPermitted extends BaseCustomException{
    public RideStatusChangeNotPermitted(String customMessage) {
        super(HttpStatus.BAD_REQUEST.value(), customMessage);
    }
}
