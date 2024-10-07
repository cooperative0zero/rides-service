package com.modsen.software.rides.kafka.consumer;

import com.modsen.software.rides.kafka.event.BaseDriverEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DriverConsumer {

    @KafkaListener(topics = {"driver_events"}, groupId = "ridesConsumerGroup")
    public void listenGroup(BaseDriverEvent message) throws Exception {
        System.out.println(message);
    }
}
