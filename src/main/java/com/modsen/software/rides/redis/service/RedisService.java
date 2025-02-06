package com.modsen.software.rides.redis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final ValueOperations<String, Object> valueOperations;

    @Value("${spring.data.redis.ttl.seconds}")
    private int ttlSeconds;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void putValue(String key, Object value) {
        valueOperations.set(key, value, Duration.ofSeconds(ttlSeconds));
    }

    public Object getValue(String key) {
        return valueOperations.get(key);
    }

    public void evictValue(String key) {
        valueOperations.getAndDelete(key);
    }
}
