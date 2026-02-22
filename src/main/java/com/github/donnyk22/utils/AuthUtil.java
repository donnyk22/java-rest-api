package com.github.donnyk22.utils;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

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
