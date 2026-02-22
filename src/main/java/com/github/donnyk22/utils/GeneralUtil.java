package com.github.donnyk22.utils;

import jakarta.servlet.http.HttpServletRequest;

public class GeneralUtil {

    public static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
}
