package com.modsen.software.rides.kafka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.rides.kafka.event.BaseRideEvent;
import com.modsen.software.rides.kafka.event.SelectionDriverEvent;
import com.modsen.software.rides.kafka.event.StatusChangedEvent;
import org.apache.kafka.common.serialization.Serializer;

public class RideEventSerializer implements Serializer<BaseRideEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, BaseRideEvent baseRideEvent) {
        try {
            if (baseRideEvent instanceof SelectionDriverEvent) {
                return objectMapper.writeValueAsBytes(baseRideEvent);
            } else if (baseRideEvent instanceof StatusChangedEvent) {
                return objectMapper.writeValueAsBytes(baseRideEvent);
            }
            throw new IllegalArgumentException("Unknown type: " + baseRideEvent.getClass());
        } catch (Exception e) {
            throw new RuntimeException("Error serializing MyCustomObject", e);
        }
    }
}
