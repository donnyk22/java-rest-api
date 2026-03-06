package com.github.donnyk22.utils;

import java.util.Collections;
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

    private Map<String, Object> getDetails() {
        Object details = getAuth().getDetails();
        if (details instanceof Map) {
            return (Map<String, Object>) details;
        }
        return Collections.emptyMap();
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

    public String getSessionId(){
        return (String) getDetails().get("sessionId");
    }

}
