package com.modsen.software.rides.mapper;

import com.modsen.software.rides.dto.RideResponse;
import com.modsen.software.rides.entity.Ride;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RideToRideResponse implements Converter<Ride, RideResponse> {

    @Override
    public RideResponse convert(Ride source) {
        return RideResponse.builder()
                .id(source.getId())
                .price(source.getPrice())
                .completionDate(source.getCompletionDate())
                .creationDate(source.getCreationDate())
                .departureAddress(source.getDepartureAddress())
                .rideStatus(source.getRideStatus().toString())
                .destinationAddress(source.getDestinationAddress())
                .driverId(source.getDriverId())
                .currency(source.getCurrency().toString())
                .passengerId(source.getPassengerId())
                .build();
    }
}
