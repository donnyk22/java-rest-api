package com.github.donnyk22.models.forms.users;

import com.github.donnyk22.models.forms.SearchForm;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class UsersFindForm extends SearchForm {
    private String role;
    private boolean isActive;
}
