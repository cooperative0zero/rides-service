package com.modsen.software.rides.kafka.event;

import lombok.Data;

@Data
public abstract class BaseRideEvent {
    protected Long rideId;
    protected String type;
}
