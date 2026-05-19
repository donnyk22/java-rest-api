package com.github.donnyk22.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.services.mfa.MfaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Tag(name = "MFA Authentication", description = "User authentication with MFA implementation")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mfa")
@Validated
public class MfaController {

    private final MfaService mfaService;

    @Operation(summary = "User login", description = "Minimal user login and return temporary credentials to verify MFA.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MstUsersDto>> loginMfa(@RequestBody @Valid UserLoginForm form) {
        MstUsersDto result = mfaService.loginMfa(form);
        String message = "Login successfully";
        if (Boolean.TRUE.equals(result.getMfaEnabled())) {
            message = "Login successfully, now please verify MFA code. Valid for 5 minutes";
        }
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(), message, result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Veryfy MFA code", description = "Verify MFA code and return final user credentials.")
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<MstUsersDto>> verifyMfa(
            @RequestParam @NotBlank(message = "Code is required") String code) {
        MstUsersDto result = mfaService.verifyMfa(code);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Login successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "MFA QR code generator", description = "Generate QR code to enable MFA and scan it with Authenticator app service.")
    @PostMapping(value = "/qr-code", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> mfaQrCodeGenerate() {
        byte[] result = mfaService.mfaQrCodeGenerate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"mfa-qrcode.png\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(result);
    }

}
