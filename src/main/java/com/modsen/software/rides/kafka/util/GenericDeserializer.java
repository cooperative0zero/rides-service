package com.modsen.software.rides.kafka.util;

import com.modsen.software.rides.kafka.configuration.KafkaTopics;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class GenericDeserializer extends JsonDeserializer<Object> {

    public GenericDeserializer() {}

    @Override
    public Object deserialize(String topic, Headers headers, byte[] data)
    {
        switch (topic)
        {
            case KafkaTopics.DRIVER_TOPIC:
                try (DriverDeserializer driverDeserializer = new DriverDeserializer()) {
                    return driverDeserializer.deserialize(topic, data);
                }
        }
        return super.deserialize(topic, data);
    }
}
