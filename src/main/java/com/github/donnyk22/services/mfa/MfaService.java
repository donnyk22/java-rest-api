package com.github.donnyk22.services.mfa;

import com.github.donnyk22.models.dtos.MstUsersDto;
import com.github.donnyk22.models.forms.users.UserLoginForm;

public interface MfaService {
    MstUsersDto loginMfa(UserLoginForm form);
    MstUsersDto verifyMfa(String code);
    byte[] mfaQrCodeGenerate();
}
