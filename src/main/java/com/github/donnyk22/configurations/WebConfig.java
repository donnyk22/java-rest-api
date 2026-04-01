package com.github.donnyk22.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//This configuration is for registering/activating interceptors (to enable end-point logging)

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // This activates the interceptor for ALL paths
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }
}