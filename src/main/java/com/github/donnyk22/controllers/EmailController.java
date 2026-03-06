package com.github.donnyk22.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.forms.EmailForm;
import com.github.donnyk22.services.email.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Email service APIs",
    description = "Send email using Mailtrap SMTP server"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    @Operation(
        summary = "Send simple email",
        description = "The email is not sent to any real recipient. It is sent to the Mailtrap inbox."
    )
    @PostMapping("/send-simple")
    public CompletableFuture<ResponseEntity<ApiResponse<List<String>>>> sendEmailSimple(@RequestBody @Valid EmailForm body) {
        return emailService.sendEmailSimple(body)
        .thenApply(result -> {
            ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Email sent successfully",
                result
            );
            return ResponseEntity.ok(response);
        })
        .exceptionally(ex -> {
            ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }

    @Operation(
        summary = "Send email with template and image attachment",
        description = "The email is not sent to any real recipient. It is sent to the Mailtrap inbox."
    )
    @PostMapping("/send")
    public CompletableFuture<ResponseEntity<ApiResponse<List<String>>>> sendEmail(@RequestBody @Valid EmailForm body) {
        return emailService.sendEmail(body)
        .thenApply(result -> {
            ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Email sent successfully",
                result
            );
            return ResponseEntity.ok(response);
        })
        .exceptionally(ex -> {
            ApiResponse<List<String>> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }
}
