package com.modsen.software.rides.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record RideReportRequest(
        @JsonProperty("driverId")
        Long driverId,

        @JsonProperty("passengerId")
        Long passengerId,

        @JsonProperty("statusTypes")
        String[] statusTypes,

        @JsonProperty("fromDate")
        OffsetDateTime fromDate,

        @JsonProperty("toDate")
        OffsetDateTime toDate,

        @Email
        @NotNull
        @JsonProperty("email")
        String email,

        @Min(value = 0)
        @JsonProperty("maxRowsCount")
        Integer maxRowsCount
) {
}
