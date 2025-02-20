package com.modsen.software.rides.service.aggregator;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class CommitsReader implements ItemReader<List<String>> {

    private final KafkaConsumer<String, String> rideDbChangesConsumer;

    @Override
    public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ConsumerRecords<String, String> records = rideDbChangesConsumer.poll(Duration.ofMillis(10));

        Logger.getGlobal().info("READ - COUNT: " + records.count());

        return records.isEmpty() ? null : StreamSupport.stream(records.spliterator(), false)
                .map(ConsumerRecord::value)
                .toList();
    }
}
