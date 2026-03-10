package com.github.donnyk22.services.auth;

import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.entities.MstUsers;
import com.github.donnyk22.models.forms.users.UserLoginForm;
import com.github.donnyk22.models.forms.users.UserRegisterForm;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    MstUsersDto register(UserRegisterForm form, HttpServletRequest httpRequest);
    MstUsersDto login(UserLoginForm form, HttpServletRequest httpRequest);
    MstUsersDto refresh();
    MstUsersDto refreshToken(MstUsers user);
    Boolean logout(HttpServletRequest request);
}
