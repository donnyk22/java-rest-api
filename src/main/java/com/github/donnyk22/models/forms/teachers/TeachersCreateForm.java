package com.github.donnyk22.models.forms.teachers;

import com.github.donnyk22.models.enums.UserGender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TeachersCreateForm {
    private Integer userId;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotNull(message = "Gender is required")
    @Schema(implementation = UserGender.class, allowableValues = {"M", "F"})
    private UserGender gender;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Address is required")
    private String address;
    private String photo;
}
