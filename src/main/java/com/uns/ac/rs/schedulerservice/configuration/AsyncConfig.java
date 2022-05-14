package com.uns.ac.rs.schedulerservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@EnableScheduling
@Configuration
public class AsyncConfig {

    public static final String TENNIS_SCHEDULER_EXECUTOR = "TennisSchedulerExecutor";

    @Bean(name = TENNIS_SCHEDULER_EXECUTOR)
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix(TENNIS_SCHEDULER_EXECUTOR + "-");
        executor.initialize();
        return executor;
    }
}
