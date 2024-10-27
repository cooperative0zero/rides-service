package com.modsen.software.rides.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SelectionDriverEvent extends BaseRideEvent{
    public SelectionDriverEvent(Long rideId) {
        this.rideId = rideId;
        this.type = this.getClass().getSimpleName();
    }
}
