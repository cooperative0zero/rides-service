package com.modsen.software.rides.util;

import com.modsen.software.rides.dto.PassengerResponse;
import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;

import java.time.OffsetDateTime;

public class Constants {
    public static final Ride ride;
    public static final Ride anotherRide;
    public static final RideRequest rideRequest;
    public static final RideResponse rideResponse;
    public static final RideResponse anotherRideResponse;

    public static final PassengerResponse passengerResponse;
    public static final PassengerResponse anotherPassengerResponse;

    static {
        ride = Ride.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .departureAddress("Departure address 1")
                .destinationAddress("Destination address 1")
                .creationDate(OffsetDateTime.now())
                .rideStatus(RideStatus.CREATED)
                .build();

        anotherRide = Ride.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .departureAddress("Departure address 2")
                .destinationAddress("Destination address 2")
                .creationDate(OffsetDateTime.now())
                .rideStatus(RideStatus.CREATED)
                .build();

        rideRequest = new RideRequest();
        rideRequest.setId(1L);
        rideRequest.setPassengerId(1L);
        rideRequest.setDepartureAddress("Departure address 1");
        rideRequest.setDestinationAddress("Destination address 1");
        rideRequest.setRideStatus(RideStatus.CREATED.name());
        rideRequest.setCreationDate(OffsetDateTime.now());

        rideResponse = RideResponse.builder()
                .id(1L)
                .passengerId(1L)
                .driverId(1L)
                .departureAddress("Departure address 1")
                .destinationAddress("Destination address 1")
                .creationDate(OffsetDateTime.now())
                .rideStatus(RideStatus.CREATED.name())
                .build();

        anotherRideResponse = RideResponse.builder()
                .id(2L)
                .passengerId(2L)
                .driverId(2L)
                .departureAddress("Departure address 2")
                .destinationAddress("Destination address 2")
                .creationDate(OffsetDateTime.now())
                .rideStatus(RideStatus.CREATED.name())
                .build();

        passengerResponse = new PassengerResponse(
                1L,
                "First Middle Last",
                "example@mail.com",
                "987654321",
                false
        );

        anotherPassengerResponse = new PassengerResponse(
                2L,
                "First2 Middle2 Last2",
                "example2@mail.com",
                "123456789",
                false
        );
    }
}
