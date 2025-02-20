package com.modsen.software.rides.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Builder(toBuilder = true)
@Schema(description = "Ride response")
public class RideResponse {
    @Schema(description = "Identifier")
    private Long id;

    @Schema(description = "Driver's identifier")
    private Long driverId;

    @Schema(description = "Passenger's identifier")
    private Long passengerId;

    @Schema(description = "Departure address of the ride")
    private String departureAddress;

    @Schema(description = "Destination address of the ride")
    private String destinationAddress;

    @Schema(description = "Current status of the ride")
    private String rideStatus;

    @Schema(description = "Creation date")
    private OffsetDateTime creationDate;

    @Schema(description = "Completion date")
    private OffsetDateTime completionDate;

    @Schema(description = "Calculated price of the ride")
    private BigDecimal price;

    @Schema(description = "Price currency")
    private String currency;
}