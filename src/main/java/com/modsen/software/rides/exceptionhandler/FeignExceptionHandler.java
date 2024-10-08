package com.modsen.software.rides.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonView;
import com.modsen.software.rides.exception.BaseCustomException;
import feign.FeignException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @JsonView(BaseCustomException.class)
    public BaseCustomException handleFeignException(FeignException e) {
        return new BaseCustomException(e.status(), e.getMessage());
    }
}
