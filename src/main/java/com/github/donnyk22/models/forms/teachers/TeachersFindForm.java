package com.github.donnyk22.models.forms.teachers;

import com.github.donnyk22.models.forms.SearchForm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TeachersFindForm extends SearchForm {
    @Schema(example = "2025/2026", description = "Academic Year (YYYY/YYYY)")
    private String academicYear;
}
