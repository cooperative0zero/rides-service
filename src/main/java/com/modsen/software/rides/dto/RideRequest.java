package com.modsen.software.rides.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import com.modsen.software.rides.dto.ValidationMarker.OnCreate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(description = "Ride request")
public class RideRequest {

    @Min(1)
    @Schema(description = "Identifier")
    private Long id;

    @Min(value = 1)
    @Null(groups = OnCreate.class)
    @Schema(description = "Driver's identifier")
    private Long driverId;

    @Min(1)
    @Schema(description = "Passenger's identifier")
    private Long passengerId;

    @NotBlank
    @Schema(description = "Departure address of the ride")
    private String departureAddress;

    @NotBlank
    @Schema(description = "Destination address of the ride")
    private String destinationAddress;

    @NotBlank
    @Schema(description = "Current status of the ride")
    private String rideStatus;

    @Past
    @Schema(description = "Creation date")
    private OffsetDateTime creationDate;

    @Null(groups = OnCreate.class)
    @Schema(description = "Completion date")
    private OffsetDateTime completionDate;

    @Null(groups = OnCreate.class)
    @Schema(description = "Calculated price of the ride")
    private BigDecimal price;
}

