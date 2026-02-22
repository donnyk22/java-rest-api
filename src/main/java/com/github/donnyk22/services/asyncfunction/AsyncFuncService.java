package com.github.donnyk22.services.asyncfunction;

import java.util.concurrent.CompletableFuture;

import com.github.donnyk22.models.dtos.AsyncJobResult;

public interface AsyncFuncService {
    CompletableFuture<String> sendEmailDummy(String email);
    CompletableFuture<Void> sendEmailDummyWithJobId(String jobId, String email);
    AsyncJobResult getJobStatus(String jobId);
    void setJobStatus(String jobId, String status);
    void sendEmailDummyWithJobIdAndMsBroker(String jobId, String email);
    void processEmailDummyWithJobIdAndMsBroker(byte[] object);
}
