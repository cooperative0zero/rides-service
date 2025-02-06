package com.modsen.software.rides.redis.configuration;
import com.modsen.software.rides.redis.util.JacksonConfig;
import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@RequiredArgsConstructor
@EnableCaching
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final ClusterConfigurationProperties clusterConfigurationProperties;

    @Bean
    protected LettuceConnectionFactory redisConnectionFactorySentinel() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(redisProperties.getSentinel().getMaster());

        redisProperties.getSentinel().getNodes().forEach(s -> sentinelConfig.sentinel(s.split(":")[0], Integer.valueOf(s.split(":")[1])));
        sentinelConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    protected LettuceConnectionFactory redisConnectionFactoryCluster(RedisClusterConfiguration redisConfiguration) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        return new LettuceConnectionFactory(redisConfiguration, clientConfig);
    }

    @Bean
    public RedisClusterConfiguration redisConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterConfigurationProperties.getNodes());
        redisClusterConfiguration.setMaxRedirects(clusterConfigurationProperties.getMaxRedirects());

        return redisClusterConfiguration;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(JacksonConfig.objectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactoryCluster(redisConfiguration()));
        return redisTemplate;
    }
}
