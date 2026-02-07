package com.example.learning.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class AppConfig {

    @Bean
    public AtomicLong counterId() {
        return new AtomicLong(0);
    }

    @Bean
    public Map<Long, Reservation> reservationMap() {
        return new ConcurrentHashMap<>();
    }
}