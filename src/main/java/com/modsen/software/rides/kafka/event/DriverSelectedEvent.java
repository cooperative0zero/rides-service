package com.modsen.software.rides.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DriverSelectedEvent extends BaseDriverEvent {
    private Long rideId;

    public DriverSelectedEvent(Long driverId, Long rideId) {
        this.driverId = driverId;
        this.rideId = rideId;
        this.type = this.getClass().getSimpleName();
    }
}
