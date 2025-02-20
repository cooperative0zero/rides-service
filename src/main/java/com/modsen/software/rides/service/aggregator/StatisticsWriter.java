package com.modsen.software.rides.service.aggregator;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticsWriter implements ItemWriter<List<String>> {

    private final KafkaConsumer<String, String> rideDbChangesConsumer;

    @Override
    public void write(Chunk<? extends List<String>> chunk) throws Exception {
        rideDbChangesConsumer.commitSync();
    }
}
