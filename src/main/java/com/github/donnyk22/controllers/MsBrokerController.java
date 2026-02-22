package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.forms.MsBrokerForm;
import com.github.donnyk22.services.msbroker.MsBrokerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@Tag(
    name = "Message Broker APIs",
    description = "Message Broker implementation APIs for testing purpose"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/ms-broker")
public class MsBrokerController {

    private final MsBrokerService msBrokerService;

    @Operation(
        summary = "Send message to object topic",
        description = "Send a message to object topic."
    )
    @PostMapping("/topic/object")
    public ResponseEntity<ApiResponse<MsBrokerForm>> sendToTopicObject(@RequestBody @Valid MsBrokerForm object) {
        MsBrokerForm result = msBrokerService.sendToTopicObject(object);
        ApiResponse<MsBrokerForm> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Message sent to topic object successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Send message to text topic",
        description = "Send a message to text topic."
    )
    @PostMapping("/topic/text")
    public ResponseEntity<ApiResponse<String>> sendToTopicText(@RequestBody @Valid @NotBlank(message = "message is required") String text) {
        String result = msBrokerService.sendToTopicText(text);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Message sent to topic text successfully",
            result);
        return ResponseEntity.ok(response);
    }
}
