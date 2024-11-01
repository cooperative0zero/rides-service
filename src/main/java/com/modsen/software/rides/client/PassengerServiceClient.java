package com.modsen.software.rides.client;

import com.modsen.software.rides.configuration.FeignConfig;
import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.exception.BaseCustomException;
import com.modsen.software.rides.exception.ServiceNotAvailable;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service")
public interface PassengerServiceClient {

    @GetMapping(value = "/api/v1/passengers/{id}")
    @CircuitBreaker(name = "passengerServiceCircuitBreaker", fallbackMethod = "handleGetPassengerByIdFallback")
    PassengerResponse getById(@PathVariable("id") Long id);

    default PassengerResponse handleGetPassengerByIdFallback(Long id, Throwable ex) {
        if (ex instanceof FeignException && ((FeignException) ex).status() == HttpStatus.NOT_FOUND.value()) {
            throw new BaseCustomException(HttpStatus.NOT_FOUND.value(), String.format("Passenger with id = %d not found", id));
        }
        throw new ServiceNotAvailable("Passenger service not available");
    }
}
