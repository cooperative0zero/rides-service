package com.modsen.software.rides.redis.aspect;

import com.modsen.software.rides.entity.Ride;
import com.modsen.software.rides.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectRedis {

    private final RedisService redisService;

    @Pointcut("@annotation(com.modsen.software.rides.redis.aspect.CacheableRide)")
    public void callCacheableMethod() {}

    @SneakyThrows
    @Around("callCacheableMethod()")
    public Object aroundCallCacheableMethod(ProceedingJoinPoint proceedingJoinPoint) {
        CacheAction action = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod()
                .getAnnotation(CacheableRide.class).action();
        switch (action) {
            case EVICT -> {
                String key = ((Long) proceedingJoinPoint.getArgs()[0]).toString();
                redisService.evictValue(key);

                return null;
            }
            case PUT -> {
                Ride ride = (Ride) proceedingJoinPoint.proceed();
                redisService.putValue(ride.getId().toString(), ride);

                return ride;
            }
            case GET -> {
                String key = ((Long) proceedingJoinPoint.getArgs()[0]).toString();
                Ride cachedRide = (Ride) redisService.getValue(key);

                if (cachedRide != null) {
                    return cachedRide;
                }

                Ride ride = (Ride) proceedingJoinPoint.proceed();

                redisService.putValue(key, ride);

                return ride;
            }
            default -> {
                return null;
            }
        }
    }
}
