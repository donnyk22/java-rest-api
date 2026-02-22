package com.github.donnyk22.models.forms.students;

import com.github.donnyk22.models.forms.SearchForm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class StudentsFindForm extends SearchForm {
    private String academicYear;
}
