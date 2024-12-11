package com.modsen.software.rides.controller;

import com.modsen.software.rides.dto.*;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.service.RideReportService;
import com.modsen.software.rides.service.RideServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Ride controller", description="Required for rides management")
public class RideController {

    private final RideServiceImpl rideService;

    private final ConversionService conversionService;

    private final RideReportService rideReportService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get all rides",
            description = "Allows to get rides values using pagination."
    )
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
    @Operation(
            summary = "Get all rides by passenger's identifier",
            description = "Allows to get rides values by passenger's identifier using pagination."
    )
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
    @Operation(
            summary = "Get all rides by driver's identifier",
            description = "Allows to get rides values by driver's identifier using pagination."
    )
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
    @Operation(
            summary = "Get ride by identifier",
            description = "Allows to get ride by identifier."
    )
    public RideResponse getById(@PathVariable @Min(1) Long id) {
        return conversionService.convert(rideService.getById(id), RideResponse.class);
    }

    @PostMapping
    @Validated(ValidationMarker.OnCreate.class)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create new ride",
            description = "Allows to create new ride."
    )
    public RideResponse save(@RequestBody @Valid RideRequest rideRequest) {
        return conversionService.convert(rideService.save(
                Objects.requireNonNull(conversionService.convert(rideRequest, Ride.class))), RideResponse.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update ride",
            description = "Allows to update ride."
    )
    public RideResponse update(@RequestBody @Valid RideRequest rideRequest) {
        return conversionService.convert(rideService.update(
                Objects.requireNonNull(conversionService.convert(rideRequest, Ride.class))), RideResponse.class);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Change ride's status",
            description = "Allows to change ride's status."
    )
    public RideResponse changeStatus(
            @PathVariable @Min(1) Long id,
            @RequestParam Long userId,
            @RequestParam String status) {
        return conversionService.convert(
                rideService.changeStatus(id, userId, RideStatus.valueOf(status.toUpperCase())), RideResponse.class);
    }

    @PostMapping("/report/excel")
    public void downloadExcelReport(RideReportRequest rideReportRequest) {
        rideReportService.generateRideReport(
                rideReportRequest.driverId(),
                rideReportRequest.passengerId(),
                rideReportRequest.fromDate(),
                rideReportRequest.toDate(),
                rideReportRequest.statusTypes(),
                rideReportRequest.email(),
                rideReportRequest.maxRowsCount()
        );
    }
}
