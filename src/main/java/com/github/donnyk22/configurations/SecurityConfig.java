package com.github.donnyk22.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilterConfig jwtAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // Allow async requests
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/logout",
                    "/ws/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        // Other filter chains configurations
        http.headers(headers -> headers
            // XSS (Cross-Site Scripting) Protection
            .contentSecurityPolicy(csp ->
                csp.policyDirectives("default-src 'self'")
            )
            // Clickjacking Protection (iFrame)
            .frameOptions(frame -> frame.deny())
            // MIME-Sniffing Protection
            .contentTypeOptions(Customizer.withDefaults())
            // Must Use HTTPS (1 year)
            // .httpStrictTransportSecurity(hsts ->
            //     hsts.includeSubDomains(true).maxAgeInSeconds(31536000)
            // )
            // Referrer Policy
            .referrerPolicy(referrer ->
                referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy
                    .STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            )
        );

        // Enable CORS Configuration
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // CORS Configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "https://donnyk22.com",
            "http://localhost:8080"
        ));
        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE"
        ));
        config.setAllowedHeaders(List.of(
            "Authorization", "Content-Type"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
