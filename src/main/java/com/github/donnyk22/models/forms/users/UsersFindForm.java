package com.github.donnyk22.models.forms.users;

import com.github.donnyk22.models.enums.UserRole;
import com.github.donnyk22.models.forms.SearchForm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UsersFindForm extends SearchForm {
    @Schema(implementation = UserRole.class, allowableValues = {"ADMIN", "STUDENT", "TEACHER"})
    private String role;
    private boolean isActive;
}
