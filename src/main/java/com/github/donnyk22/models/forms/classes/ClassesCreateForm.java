package com.github.donnyk22.models.forms.classes;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ClassesCreateForm {
    @NotBlank(message = "Class name is required")
    private String className;
    @NotBlank(message = "Grade level is required")
    private String gradeLevel;
    @NotBlank(message = "Academic year is required")
    private String academicYear;
}
