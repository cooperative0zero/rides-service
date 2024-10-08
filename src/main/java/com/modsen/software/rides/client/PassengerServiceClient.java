package com.modsen.software.rides.client;

import com.modsen.software.rides.configuration.FeignConfig;
import com.modsen.software.rides.dto.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "passenger-service",
        configuration = FeignConfig.class)
public interface PassengerServiceClient {

    @GetMapping(value = "/api/v1/passengers/{id}")
    PassengerResponse getById(@PathVariable("id") Long id);
}
