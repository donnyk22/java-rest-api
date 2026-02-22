package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.forms.users.UserRegisterForm;
import com.github.donnyk22.services.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(
    name = "Authentication",
    description = "User authentication and session management APIs"
)
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private final AuthService authService;

    @Operation(
        summary = "Register user",
        description = "Create a new user account."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UsersDto>> register(@RequestBody @Valid UserRegisterForm form, HttpServletRequest httpRequest) {
        UsersDto result = authService.register(form, httpRequest);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Register successfully. Please login with your credential",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User login",
        description = "Authenticate user and return credentials."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UsersDto>> login(@RequestBody @Valid UserLoginForm form, HttpServletRequest httpRequest) {
        UsersDto result = authService.login(form, httpRequest);
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Login successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Refresh credentials",
        description = "Refresh authentication credentials."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<UsersDto>> refresh() {
        UsersDto result = authService.refresh();
        ApiResponse<UsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Credential refreshed successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User logout",
        description = "Invalidate current user session."
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request) {
            Boolean result = authService.logout(request);
            ApiResponse<Boolean> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Logout successfully",
                result);
            return ResponseEntity.ok(response);
    }

}
