package com.modsen.software.rides.exception;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BaseCustomException extends RuntimeException {

    @JsonView(BaseCustomException.class)
    public int statusCode;

    @JsonView(BaseCustomException.class)
    public String customMessage;
}
