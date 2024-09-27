package com.modsen.software.rides.exception;

import org.springframework.http.HttpStatus;

public class RideDowngradingStatus extends BaseCustomException{
    public RideDowngradingStatus(String customMessage) {
        super(HttpStatus.BAD_REQUEST.value(), customMessage);
    }
}
