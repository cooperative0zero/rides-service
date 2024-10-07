package com.modsen.software.rides.kafka.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.modsen.software.rides.kafka.event.BaseDriverEvent;
import com.modsen.software.rides.kafka.event.DriverSelectedEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class DriverDeserializer implements Deserializer<BaseDriverEvent> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BaseDriverEvent deserialize(String topic, byte[] data) {
        try {
            Map<String, Object> map = objectMapper.readValue(data, new TypeReference<>() {});
            String type = (String) map.get("type");

            if ("DriverSelectedEvent".contentEquals(type)) {
                return objectMapper.readValue(data, DriverSelectedEvent.class);
            }

            throw new IllegalArgumentException("Unknown type: " + type);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing ", e);
        }
    }
}
