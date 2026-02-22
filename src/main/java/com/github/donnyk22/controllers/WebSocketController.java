package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.WebSocketUserSessionDto;
import com.github.donnyk22.models.forms.WebSocketForm;
import com.github.donnyk22.models.forms.WebSocketUsersForm;
import com.github.donnyk22.services.websocket.WebSocketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(
    name = "WebSocket APIs",
    description = "WebSocket implementation APIs for testing purpose"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/ws")
public class WebSocketController {

    private final WebSocketService websocketService;

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public WebSocketForm topic(WebSocketForm message) {
        return message;
    }

    @MessageMapping("/messages/users")
    @SendTo("/queue/messages")
    public WebSocketUsersForm topicUsers(WebSocketUsersForm message) {
        return message;
    }

    @Operation(
        summary = "Send message to all users",
        description = "Broadcast a message to all connected WebSocket users."
    )
    @PostMapping()
    public ResponseEntity<ApiResponse<WebSocketForm>> sendMessages(@RequestBody @Valid WebSocketForm message) {
        WebSocketForm result = websocketService.sendMessages(message);
        ApiResponse<WebSocketForm> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Message broadcasted successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Send message to specific users",
        description = "Send a message to specific WebSocket users."
    )
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<WebSocketForm>> sendMessagesToUsers(@RequestBody @Valid WebSocketUsersForm message) {
        WebSocketForm result = websocketService.sendMessagesToUsers(message);
        ApiResponse<WebSocketForm> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Message sent to specific users successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get active users [Admin Only]",
        description = "Retrieve all active WebSocket users."
    )
    @PreAuthorize("hasAuthority(UserRoles.ADMIN)")
    @GetMapping("/users/online")
    public ResponseEntity<ApiResponse<WebSocketUserSessionDto>> getActiveUsers() {
        WebSocketUserSessionDto result = websocketService.getActiveUsers();
        ApiResponse<WebSocketUserSessionDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "User sessions retrieved successfully",
            result);
        return ResponseEntity.ok(response);
    }

}
