package com.modsen.software.rides.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PassengerResponse {
    private Long id;

    private String fmlNames;

    private String email;

    private String phone;

    private Boolean isDeleted;
}