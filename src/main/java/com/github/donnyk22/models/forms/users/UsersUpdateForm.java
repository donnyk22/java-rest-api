package com.github.donnyk22.models.forms.users;

import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.models.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UsersUpdateForm {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    private String email;
    @NotNull(message = "Role is required")
    @Schema(implementation = UserRole.class, allowableValues = {"ADMIN", "STUDENT", "TEACHER"})
    private UserRole role;
    private MultipartFile photo;
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    @NotNull(message = "Version is required")
    @Schema(example = "1")
    private Integer version;
}
