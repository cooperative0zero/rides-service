package com.modsen.software.rides.client;

import com.modsen.software.rides.dto.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "passenger-service", url = "http://passenger-service:8081")
public interface PassengerServiceClient {

    @GetMapping("/api/v1/passengers")
    PassengerResponse getById(@PathVariable("id") Long id);
}
