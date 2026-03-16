package com.github.donnyk22.services.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.github.donnyk22.models.dtos.MstUsersDto;

public interface OAuth2Service {

    MstUsersDto OAuth2GetInfo(OAuth2User principal);
    
}
