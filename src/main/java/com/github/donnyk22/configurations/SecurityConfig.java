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
            // If in the front end you are using cookies for authentication, you should enable CSRF protection
            // Disable CRSF if using local storage or session storage for authentication tokens (e.g., JWT in Authorization header)
            // .csrf(csrf -> csrf
            //     .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) 
            //     .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            // )
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // Allow async requests
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/api/v1/auth/register",
                    "/api/v1/auth/login",
                    "/api/v1/auth/logout",
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
            // Origin Must Use HTTPS (with duration of 1 year)
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
        // Allow specific origins only (e.g., your frontend app domains)
        config.setAllowedOrigins(List.of(
            "https://donnyk22.com",
            "http://localhost:8080"
        ));
        // Allow specific HTTP methods
        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE"
        ));
        // Allow specific headers (e.g., Authorization for JWT tokens)
        config.setAllowedHeaders(List.of(
            "Authorization", "Content-Type"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
