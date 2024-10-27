package com.modsen.software.rides.configuration;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, SECONDS.toMillis(1), 5);
    }
}