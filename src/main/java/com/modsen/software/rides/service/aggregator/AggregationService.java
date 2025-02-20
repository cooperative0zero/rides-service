package com.modsen.software.rides.service.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class AggregationService {

    private final Job groupRidesByDriverByMonth;
    private final JobLauncher jobLauncher;
    private boolean isJobRunning = false;

    @SneakyThrows
    @Scheduled(fixedRate = 10000)
    public void runGroupingDriversJob() {

        if (!isJobRunning) {
            isJobRunning = true;
            jobLauncher.run(groupRidesByDriverByMonth, new JobParametersBuilder()
                    .addLong("timestamp", System.nanoTime()).toJobParameters());
            isJobRunning = false;
        }
    }
}
