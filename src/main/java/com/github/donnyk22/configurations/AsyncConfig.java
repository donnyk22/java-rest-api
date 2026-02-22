package com.github.donnyk22.configurations;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${app.async.max-worker}")
    private Integer MAX_WORKER;

    @Value("${app.async.max-queue}")
    private Integer MAX_QUEUE;

    @Bean
    public Executor workerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(MAX_WORKER); //minimum number of threads the pool tries to keep alive
        executor.setMaxPoolSize(MAX_WORKER); //maximum number of threads the pool is allowed to create
        executor.setQueueCapacity(MAX_QUEUE); //maximum number of tasks that can be queued. more than this will be rejected (TaskRejectedException/RejectedExecutionException)
        executor.setThreadNamePrefix("Worker-");
        executor.initialize();
        return executor;
    }
}