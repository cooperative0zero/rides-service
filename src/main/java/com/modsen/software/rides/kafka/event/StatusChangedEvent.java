package com.modsen.software.rides.kafka.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatusChangedEvent extends BaseRideEvent {
    private String fromStatus;
    private String toStatus;
    private String userType;

    public StatusChangedEvent(Long rideId, String fromStatus, String toStatus, String userType) {
        this.rideId = rideId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.userType = userType;
        this.type = this.getClass().getSimpleName();
    }
}
