package com.modsen.software.rides.service.aggregator;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RidesAggregatorConfig {
    private static final int CHUNK_SIZE = 1;

    @Bean
    public Job groupRidesByDriverByMonth(JobRepository jobRepository, Step groupStep) {
        return new JobBuilder("groupRidesByDriverJob", jobRepository)
                .start(groupStep)
                .build();
    }

    @Bean
    public Step groupStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                           CommitsReader reader, CommitProcessor processor, StatisticsWriter writer) {
        return new StepBuilder("groupStep", jobRepository)
                .<List<String>, List<String>>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}
