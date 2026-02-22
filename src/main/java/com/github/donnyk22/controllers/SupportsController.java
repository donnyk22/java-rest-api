package com.github.donnyk22.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.services.supports.SupportsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;

@Tag(
    name = "Supports",
    description = "System and maintenance support APIs"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/supports")
public class SupportsController {

    private final SupportsService supportsService;

    @Operation(
        summary = "Check Redis connection",
        description = "Verify Redis connectivity and status."
    )
    @PostMapping("/redis-check-connection")
    public ResponseEntity<ApiResponse<String>> redisCheckConnection() {
        String result = supportsService.redisCheckConnection();
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Checking status success",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Check login credential",
        description = "Retrieve active user login credential details."
    )
    @PostMapping("/user-check-login-credential")
    public ResponseEntity<ApiResponse<Map<String, Object>>> orders() {
        Map<String, Object> result = supportsService.checkUserLoginCredential();
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Checking active login credential success",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Get system bean list",
        description = "Retrieve all registered Spring beans."
    )
    @GetMapping("/system-get-bean-list")
    public ResponseEntity<ApiResponse<List<String>>> getBeanList() {
        List<String> result = supportsService.getBeanList();
        ApiResponse<List<String>> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Checking Bean list success",
            result);
        return ResponseEntity.ok(response);
    }
    
}
