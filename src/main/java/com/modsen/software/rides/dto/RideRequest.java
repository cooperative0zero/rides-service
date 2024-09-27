package com.modsen.software.rides.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import com.modsen.software.rides.dto.ValidationMarker.OnCreate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {

    @Min(1)
    private Long id;

    @Min(value = 1)
    @Null(groups = OnCreate.class)
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
    private OffsetDateTime creationDate;

    @Null(groups = OnCreate.class)
    private OffsetDateTime completionDate;

    @Null(groups = OnCreate.class)
    private BigDecimal price;
}

