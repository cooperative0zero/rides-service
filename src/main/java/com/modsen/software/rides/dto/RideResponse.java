package com.modsen.software.rides.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideResponse {
    private Long id;

    private Long driverId;

    private Long passengerId;

    private String departureAddress;

    private String destinationAddress;

    private String rideStatus;

    private OffsetDateTime creationDate;

    private OffsetDateTime completionDate;

    private BigDecimal price;
}