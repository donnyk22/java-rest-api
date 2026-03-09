package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.forms.users.UserRegisterForm;
import com.github.donnyk22.services.auth.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Authentication",
    description = "User authentication and session management APIs"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    private final AuthService authService;

    @Operation(
        summary = "Register user",
        description = "Create a new user account."
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MstUsersDto>> register(@RequestBody @Valid UserRegisterForm form, HttpServletRequest httpRequest) {
        MstUsersDto result = authService.register(form, httpRequest);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Register successfully. Please login with your credential",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "User login",
        description = "Authenticate user and return credentials."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MstUsersDto>> login(@RequestBody @Valid UserLoginForm form, HttpServletRequest httpRequest) {
        MstUsersDto result = authService.login(form, httpRequest);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
            "Login successfully",
            result);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Refresh credentials",
        description = "Refresh authentication credentials."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<MstUsersDto>> refresh() {
        MstUsersDto result = authService.refresh();
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
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
            result ? "Logout successfully" : "Already logged out or session invalid",
            result);
        return ResponseEntity.ok(response);
    }

}
