package com.github.donnyk22.utils;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.github.donnyk22.exceptions.InternalServerErrorException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthUtil {

    private Authentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new InternalServerErrorException("No authentication found in security context");
        }
        return auth;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getDetails() {
        Object details = getAuth().getDetails();
        if (details instanceof Map) {
            return (Map<String, Object>) details;
        }
        return Collections.emptyMap();
    }

    public Integer getUserId() {
        Authentication auth = getAuth();
        Object principal = auth.getPrincipal();

        try {
            return Integer.parseInt(principal.toString());
        } catch (NumberFormatException e) {
            log.warn("User not found in security context");
            return null;
        }
    }

    public String getUserName() {
        return (String) getDetails().get("username");
    }

    public String getUserEmail() {
        return (String) getDetails().get("email");
    }

    public String getUserRole() {
        return (String) getDetails().get("role");
    }

    public String getSessionId() {
        return (String) getDetails().get("sessionId");
    }

}
