package com.github.donnyk22.models.forms.teachers;

import com.github.donnyk22.models.forms.SearchForm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TeachersFindForm extends SearchForm {
    private String academicYear;
}
