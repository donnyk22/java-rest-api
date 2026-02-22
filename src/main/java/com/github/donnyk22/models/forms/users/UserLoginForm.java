package com.github.donnyk22.models.forms.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UserLoginForm {
    @NotBlank(message = "Username/Email is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
