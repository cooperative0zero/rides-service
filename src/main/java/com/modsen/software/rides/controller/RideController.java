package com.modsen.software.rides.controller;

import com.modsen.software.rides.dto.PaginatedResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.dto.ValidationMarker;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.service.RideServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideServiceImpl rideService;

    private final ConversionService conversionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RideResponse> getAll(
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = rideService.getAll(pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(ride -> conversionService.convert(ride, RideResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/passengers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RideResponse> getAllByPassengerId(
            @PathVariable @Min(1) Long id,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = rideService.getAllByPassengerId(id, pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(ride -> conversionService.convert(ride, RideResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/drivers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<RideResponse> getAllByDriverId(
            @PathVariable @Min(1) Long id,
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        var result = rideService.getAllByDriverId(id, pageNumber, pageSize, sortBy, sortOrder)
                .stream()
                .map(ride -> conversionService.convert(ride, RideResponse.class))
                .toList();

        return new PaginatedResponse<>(result, pageNumber, pageSize, result.size());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse getById(@PathVariable @Min(1) Long id) {
        return conversionService.convert(rideService.getById(id), RideResponse.class);
    }

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    public RideResponse save(@RequestBody @Valid RideRequest rideRequest) {
        return conversionService.convert(rideService.save(
                Objects.requireNonNull(conversionService.convert(rideRequest, Ride.class))), RideResponse.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RideResponse update(@RequestBody @Valid RideRequest rideRequest) {
        return conversionService.convert(rideService.update(
                Objects.requireNonNull(conversionService.convert(rideRequest, Ride.class))), RideResponse.class);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RideResponse changeStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam Long userId,
            @RequestParam String status) {
        return conversionService.convert(
                rideService.changeStatus(id, userId, RideStatus.valueOf(status.toUpperCase())), RideResponse.class);
    }
}
