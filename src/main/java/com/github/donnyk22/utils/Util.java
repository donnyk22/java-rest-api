package com.github.donnyk22.utils;

import com.github.donnyk22.exceptions.ConflictException;

import jakarta.servlet.http.HttpServletRequest;

public class Util {

    public static String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    public static void compareVersion(Integer currentVersion, Integer providedVersion) {
        if (!currentVersion.equals(providedVersion)) {
            throw new ConflictException("Data is already updated by another user. Please refresh and try again.");
        }
    }
    
}
