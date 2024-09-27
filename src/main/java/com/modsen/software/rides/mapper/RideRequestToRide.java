package com.modsen.software.rides.mapper;

import com.modsen.software.rides.dto.RideRequest;
import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.entity.enumeration.RideStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RideRequestToRide implements Converter<RideRequest, Ride> {

    @Override
    public Ride convert(RideRequest source) {
        return Ride.builder()
                .id(source.getId())
                .price(source.getPrice())
                .completionDate(source.getCompletionDate())
                .creationDate(source.getCreationDate())
                .departureAddress(source.getDepartureAddress())
                .destinationAddress(source.getDestinationAddress())
                .rideStatus(RideStatus.valueOf(source.getRideStatus().toUpperCase()))
                .driverId(source.getDriverId())
                .passengerId(source.getPassengerId())
                .build();
    }
}
