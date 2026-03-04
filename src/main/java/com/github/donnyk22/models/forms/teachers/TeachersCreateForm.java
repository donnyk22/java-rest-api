package com.github.donnyk22.models.forms.teachers;

import org.springframework.web.multipart.MultipartFile;

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
    @NotNull(message = "User ID is required")
    private Integer userId;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotNull(message = "Gender is required")
    @Schema(implementation = UserGender.class, allowableValues = {"M", "F"})
    private UserGender gender;
    @NotBlank(message = "Phone is required")
    @Schema(example = "081234567890")
    private String phone;
    @NotBlank(message = "Address is required")
    private String address;
    private MultipartFile photo;
}
