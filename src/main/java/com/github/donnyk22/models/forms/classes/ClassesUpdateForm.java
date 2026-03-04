package com.github.donnyk22.models.forms.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ClassesUpdateForm extends ClassesCreateForm {
    @NotNull(message = "Version is required")
    @Schema(example = "1")
    private Integer version;
}
