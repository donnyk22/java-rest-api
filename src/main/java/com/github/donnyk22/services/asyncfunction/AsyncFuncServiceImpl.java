package com.github.donnyk22.services.asyncfunction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.github.donnyk22.configurations.RabbitMQConfig;
import com.github.donnyk22.exceptions.ResourceNotFoundException;
import com.github.donnyk22.models.dtos.AsyncJobData;
import com.github.donnyk22.models.dtos.AsyncJobResult;
import com.github.donnyk22.models.enums.JobStatus;
import com.github.donnyk22.utils.ConverterUtil;
import com.github.donnyk22.utils.RedisTokenUtil;

@Service
public class AsyncFuncServiceImpl implements AsyncFuncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncFuncServiceImpl.class);
    
    private final RedisTokenUtil redisTokenUtil;
    private final RabbitTemplate rabbitTemplate;

    public AsyncFuncServiceImpl(RedisTokenUtil redisTokenUtil, RabbitTemplate rabbitTemplate) {
        this.redisTokenUtil = redisTokenUtil;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @Async
    public CompletableFuture<String> sendEmailDummy(String email) {
        try {
            logger.info("Sending email to: " + email);
            Thread.sleep(5000);
            logger.info("Email sent to: " + email);
            return CompletableFuture.completedFuture("Email sent successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(
                new RuntimeException("Email sending was interrupted")
            );
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> sendEmailDummyWithJobId(String jobId, String email) {
        try {
            setJobStatus(jobId, JobStatus.RUNNING.name());
            logger.info("Sending email to: " + email);
            Thread.sleep(20000);
            logger.info("Email sent to: " + email);
            setJobStatus(jobId, JobStatus.SUCCESS.name());
            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            setJobStatus(jobId, JobStatus.FAILED.name());
            return CompletableFuture.failedFuture(
                new RuntimeException("Email sending was interrupted")
            );
        } catch (Exception e) {
            setJobStatus(jobId, JobStatus.FAILED.name());
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public AsyncJobResult getJobStatus(String jobId) {
        String status = redisTokenUtil.get("AsyncJobStatus", jobId);
        if(status == null) {
            throw new ResourceNotFoundException("Job not found: " + jobId);
        }
        return new AsyncJobResult()
            .setJobId(jobId)
            .setStatus(status);
    }

    @Override
    public void setJobStatus(String jobId, String status) {
        redisTokenUtil.store("AsyncJobStatus", jobId, status, 60, TimeUnit.MINUTES);
    }

    @Override
    public void sendEmailDummyWithJobIdAndMsBroker(String jobId, String email) {
        AsyncJobData object = new AsyncJobData()
            .setJobId(jobId)
            .setEmail(email);
            
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.JOB_EXCHANGE,
            RabbitMQConfig.JOB_ROUTING_KEY,
            ConverterUtil.objectToBytes(object)
        );
    }

    // job listener with max worker
    // Async process not handled by Spring, but handled by RabbitMQ
    @Override
    @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE, concurrency = "${app.async.max-worker}")
    public void processEmailDummyWithJobIdAndMsBroker(byte[] object) {
        AsyncJobData data = ConverterUtil.bytesToObject(object, AsyncJobData.class);
        try {
            setJobStatus(data.getJobId(), JobStatus.RUNNING.name());
            logger.info("Worker " + Thread.currentThread().getName() + " processing job (sending email): " + data.getJobId());
            Thread.sleep(20000);
            logger.info("Email sent to: " + data.getEmail());
            setJobStatus(data.getJobId(), JobStatus.SUCCESS.name());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            setJobStatus(data.getJobId(), JobStatus.FAILED.name());
            throw new RuntimeException("Email sending was interrupted");
        } catch (Exception e) {
            setJobStatus(data.getJobId(), JobStatus.FAILED.name());
            throw new RuntimeException("Job failed: " + data.getJobId(), e);
        }
    }
    
}
