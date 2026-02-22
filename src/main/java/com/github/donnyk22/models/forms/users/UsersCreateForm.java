package com.github.donnyk22.models.forms.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UsersCreateForm extends UsersUpdateForm{
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Password must be at least 8 characters")
    private String password;
    @NotBlank(message = "Please retype you password")
    @Schema(description = "Please retype you password")
    private String rePassword;
}
