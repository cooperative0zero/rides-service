package com.modsen.software.rides.entity;

import com.modsen.software.rides.entity.enumeration.RideStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "rides")
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "r_id")
    private Long id;

    @Column(name = "r_driver_id", nullable = false)
    private Long driverId;

    @Column(name = "r_passenger_id", nullable = false)
    private Long passengerId;

    @Column(name = "r_departure_address", nullable = false)
    private String departureAddress;

    @Column(name = "r_destination_address", nullable = false)
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "r_status", nullable = false)
    private RideStatus rideStatus;

    @Column(name = "r_creation_date", nullable = false)
    private OffsetDateTime creationDate;

    @Column(name= "r_completion_date")
    private OffsetDateTime completionDate;

    @Column(name = "r_cost")
    private BigDecimal price;
}
