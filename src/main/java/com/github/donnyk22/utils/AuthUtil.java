package com.github.donnyk22.utils;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.github.donnyk22.exceptions.InternalServerErrorException;

@Component
public class AuthUtil {

    private Authentication getAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new InternalServerErrorException("No authentication found in security context");
        }
        return auth;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDetails(){
        return (Map<String, Object>) getAuth().getDetails();
    }

    public Integer getUserId(){
        return (Integer) getAuth().getPrincipal();
    }

    public String getUserName(){
        return (String) getDetails().get("username");
    }

    public String getUserEmail(){
        return (String) getDetails().get("email");
    }

    public String getUserRole(){
        return (String) getDetails().get("role");
    }

}
