package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.services.oauth2.OAuth2Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "OAuth2 API", description = "Login/Register with Google OAuth2 service")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

    @Operation(summary = "Get user info from Google OAuth2", description = "Get user info, and use the credentials to login or register (Please access via src/main/java/com/github/donnyk22/web/OAuth2Test.html)")
    @GetMapping()
    public ResponseEntity<ApiResponse<MstUsersDto>> OAuth2GetInfo(@AuthenticationPrincipal OAuth2User principal) {
        MstUsersDto result = oAuth2Service.OAuth2GetInfo(principal);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "OAuth2 login successfully",
                result);
        return ResponseEntity.ok(response);
    }
}
