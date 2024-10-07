package com.modsen.software.rides.kafka.event;

import lombok.Data;

@Data
public abstract class BaseDriverEvent {
    protected Long driverId;
    protected String type;
}
