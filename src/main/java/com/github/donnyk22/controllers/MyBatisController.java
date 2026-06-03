package com.github.donnyk22.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersFindForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;
import com.github.donnyk22.services.mybatis.MyBatisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "MyBatis APIs", description = "CRUD API using MyBatis")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mybatis")
@Validated // for validating primitive data types
public class MyBatisController {

    private final MyBatisService myBatisService;

    // === Users ===

    @Operation(summary = "Get users", description = "Retrieve and search users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<FindResponse<MstUsersDto>>> findUsers(@ModelAttribute @Valid UsersFindForm form) {
        FindResponse<MstUsersDto> result = myBatisService.findUsers(form);
        ApiResponse<FindResponse<MstUsersDto>> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Users retrieved successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<MstUsersDto>> readUser(
            @PathVariable Integer userId) {
        MstUsersDto result = myBatisService.readUser(userId);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "User retrieved successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create user", description = "Create a new user with profile picture")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/users", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<MstUsersDto>> createUser(@ModelAttribute @Valid UsersCreateForm form) {
        MstUsersDto result = myBatisService.createUser(form);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "User created successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user", description = "Update user details by user ID")
    @PutMapping(value = "/users/{userId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ApiResponse<MstUsersDto>> updateUser(
            @PathVariable Integer userId,
            @ModelAttribute @Valid UsersUpdateForm form) {
        MstUsersDto result = myBatisService.updateUser(userId, form);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "User updated successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user password", description = "Update user password by user ID")
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<ApiResponse<MstUsersDto>> updateUserPassword(
            @PathVariable Integer userId,
            @ModelAttribute @Valid UsersUpdatePasswordForm form) {
        MstUsersDto result = myBatisService.updateUserPassword(userId, form);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "User password updated successfully",
                result);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete user", description = "Delete a user by user ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<MstUsersDto>> deleteUser(
            @PathVariable Integer userId) {
        MstUsersDto result = myBatisService.deleteUser(userId);
        ApiResponse<MstUsersDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                "User deleted successfully",
                result);
        return ResponseEntity.ok(response);
    }
}
