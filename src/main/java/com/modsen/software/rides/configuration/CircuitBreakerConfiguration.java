package com.modsen.software.rides.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig defaultConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(100)
                .permittedNumberOfCallsInHalfOpenState(10)
                .slowCallDurationThreshold(Duration.ofSeconds(4))
                .slowCallRateThreshold(90)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .minimumNumberOfCalls(10)
                .build();

        return CircuitBreakerRegistry.of(defaultConfig);
    }
}

