package com.modsen.software.rides.controller;

import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import com.modsen.software.rides.service.RideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    @GetMapping
    public ResponseEntity<List<RideResponse>> getAll(@RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RideResponse> response = rideService.getAll(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/passengers/{id}")
    public ResponseEntity<List<RideResponse>> getAllByPassengerId(@PathVariable @Min(1) Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RideResponse> response = rideService.getAllByPassengerId(id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<List<RideResponse>> getAllByDriverId(@PathVariable @Min(1) Long id,
                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "desc") String sortOrder) {

        List<RideResponse> response = rideService.getAllByDriverId(id, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable @Min(1) Long id) {
        RideResponse response = rideService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RideResponse> save(@RequestBody @Valid RideRequest rideRequest) {
        RideResponse response = rideService.save(rideRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RideResponse> update(@RequestBody @Valid RideRequest rideRequest) {
        RideResponse response = rideService.update(rideRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<RideResponse> changeStatus(@PathVariable @Min(1) Long id, @PathVariable String status) {
        RideResponse response = rideService.changeStatus(id, RideStatus.valueOf(status.toUpperCase()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
