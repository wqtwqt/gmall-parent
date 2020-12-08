package com.atguigu.gmall.item.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){

       return new ThreadPoolExecutor(
                3,
                5,
                10L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()

        );
    }
}
