package com.github.donnyk22.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.donnyk22.utils.AuthUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//This configuration is based from WebConfig.java. After activating interceptors, it will log the user's activity

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private final AuthUtil authUtil;
        
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("User: {} | Method: {} | URI: {} | Status: {}",
            authUtil.getUserId() == null ? "Anonymous" : authUtil.getUserName(),
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus());
    }
}
