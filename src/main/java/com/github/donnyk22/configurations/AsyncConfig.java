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
    private Integer maxWorker;

    @Value("${app.async.max-queue}")
    private Integer maxQueue;

    @Bean
    public Executor workerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(maxWorker); // minimum number of threads the pool tries to keep alive
        executor.setMaxPoolSize(maxWorker); // maximum number of threads the pool is allowed to create
        executor.setQueueCapacity(maxQueue); // maximum number of tasks that can be queued. more than this will be
                                             // rejected (TaskRejectedException/RejectedExecutionException)
        executor.setThreadNamePrefix("Worker-");
        executor.initialize();
        return executor;
    }
}