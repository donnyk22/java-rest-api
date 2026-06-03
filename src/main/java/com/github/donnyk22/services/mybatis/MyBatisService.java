package com.github.donnyk22.services.mybatis;

import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.users.UsersCreateForm;
import com.github.donnyk22.models.forms.users.UsersFindForm;
import com.github.donnyk22.models.forms.users.UsersUpdateForm;
import com.github.donnyk22.models.forms.users.UsersUpdatePasswordForm;

public interface MyBatisService {

    FindResponse<MstUsersDto> findUsers(UsersFindForm form);

    MstUsersDto readUser(Integer id);

    MstUsersDto createUser(UsersCreateForm form);

    MstUsersDto updateUser(Integer id, UsersUpdateForm form);

    MstUsersDto updateUserPassword(Integer id, UsersUpdatePasswordForm form);

    MstUsersDto deleteUser(Integer id);

}
