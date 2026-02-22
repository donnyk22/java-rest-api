package com.github.donnyk22.models.forms.users;

import com.github.donnyk22.models.enums.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UserRegisterForm {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;
    @NotNull(message = "Role is required")
    @Schema(implementation = UserRole.class, allowableValues = {"STUDENT", "TEACHER"})
    private UserRole role;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @NotBlank(message = "Please retype you password")
    private String rePassword;
}
