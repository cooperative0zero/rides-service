package com.modsen.software.rides.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @Min(1)
    private Long id;

    @Min(1)
    private Long driverId;

    @Min(1)
    private Long passengerId;

    @NotBlank
    private String departureAddress;

    @NotBlank
    private String destinationAddress;

    @NotBlank
    private String rideStatus;

    @Past
    private Date creationDate;

    private Date completionDate;

    private BigDecimal price;
}

