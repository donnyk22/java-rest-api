package com.github.donnyk22.services.auth;

import com.github.donnyk22.models.dtos.UsersDto;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.forms.users.UserRegisterForm;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UsersDto register(UserRegisterForm form, HttpServletRequest httpRequest);
    UsersDto login(UserLoginForm form, HttpServletRequest httpRequest);
    UsersDto refresh();
    Boolean logout(HttpServletRequest request);
}
