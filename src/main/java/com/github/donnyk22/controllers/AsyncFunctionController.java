package com.github.donnyk22.controllers;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.AsyncJobResult;
import com.github.donnyk22.models.enums.JobStatus;
import com.github.donnyk22.services.asyncfunction.AsyncFuncService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Tag(
    name = "Async Function APIs",
    description = "Asynchronous function implementation APIs for testing purpose"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/async")
@Validated
public class AsyncFunctionController {

    private final AsyncFuncService asyncFuncService;

    @Operation(
        summary = "Send dummy email",
        description = "Send dummy email and simulate the async process in the background."
    )
    @PostMapping("/send-email")
    public CompletableFuture<ResponseEntity<ApiResponse<String>>> sendDummyEmail(
            @RequestParam 
            @NotBlank(message = "Email is required") 
            @Email(message = "Invalid email format") String email) {
        return asyncFuncService.sendEmailDummy(email)
        .thenApply(result -> {
            ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Email sent successfully",
                result
            );
            return ResponseEntity.ok(response);
        })
        .exceptionally(ex -> {
            ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }

    @Operation(
        summary = "Send dummy email with job ID",
        description = "Send dummy email and simulate the async process in the background with job ID and status"
    )
    @PostMapping("/send-email-job-id")
    public ResponseEntity<ApiResponse<String>> sendDummyEmailWithJobId(
            @RequestParam 
            @NotBlank(message = "Email is required") 
            @Email(message = "Invalid email format") String email) {
        String jobId = UUID.randomUUID().toString();
        asyncFuncService.setJobStatus(jobId, JobStatus.PENDING.name());
        asyncFuncService.sendEmailDummyWithJobId(jobId, email);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.ACCEPTED.value(),
            "Email sent in queue with job ID",
            jobId
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(
        summary = "Send dummy email with job ID and Message Broker",
        description = "Send dummy email and simulate the async process in the background with job ID and handled by Message Broker"
    )
    @PostMapping("/send-email-job-id-rabbitmq")
    public ResponseEntity<ApiResponse<String>> sendEmailDummyWithJobIdAndMsBroker(
            @RequestParam 
            @NotBlank(message = "Email is required") 
            @Email(message = "Invalid email format") String email) {
        String jobId = UUID.randomUUID().toString();
        asyncFuncService.setJobStatus(jobId, JobStatus.PENDING.name());
        asyncFuncService.sendEmailDummyWithJobIdAndMsBroker(jobId, email);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.ACCEPTED.value(),
            "Email sent in queue with job ID",
            jobId
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(
        summary = "Get email status by job ID",
        description = "Get email queue status by job ID"
    )
    @GetMapping("/status-email-job-id")
    public ResponseEntity<ApiResponse<AsyncJobResult>> getJobStatus(
            @RequestParam 
            @NotBlank(message = "Job ID is required") String jobId) {
        AsyncJobResult result = asyncFuncService.getJobStatus(jobId);
        ApiResponse<AsyncJobResult> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Job status fetched successfully",
            result
        );
        return ResponseEntity.ok(response);
    }

}
