package com.modsen.software.rides.redis.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
@Getter
@Setter
public class ClusterConfigurationProperties {
    private List<String> nodes;
    private int maxRedirects;
}
