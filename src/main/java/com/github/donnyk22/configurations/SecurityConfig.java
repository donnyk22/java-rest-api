package com.github.donnyk22.configurations;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.donnyk22.models.dtos.ApiResponse;
import com.github.donnyk22.models.enums.Action;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilterConfig jwtAuthFilter;
    private final ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Dedicated filter chain for the Scalar API reference (/scalar).
     * <p>
     * It is intentionally ordered ahead of {@link #filterChain(HttpSecurity)} so the
     * relaxed Content-Security-Policy below applies ONLY to the Scalar docs path. Every
     * other path keeps the strict {@code default-src 'self'} policy from the main chain.
     * The relaxed policy allows the Scalar assets loaded from the jsdelivr CDN.
     * <p>
     * NOTE: the exact CDN origin(s) and the minimal directive set must be confirmed by
     * opening {@code /scalar} with browser devtools and reading any CSP violation reports
     * (see change add-scalar-api-docs, task 3.3); widen only what Scalar actually needs.
     */
    @Bean
    @Order(1)
    SecurityFilterChain scalarFilterChain(HttpSecurity http) {
        String scalarCsp = "default-src 'self'; "
                + "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.jsdelivr.net; "
                + "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://fonts.googleapis.com; "
                + "font-src 'self' data: https://cdn.jsdelivr.net https://fonts.gstatic.com https://fonts.scalar.com; "
                + "img-src 'self' data: https:; "
                + "connect-src 'self' https://cdn.jsdelivr.net https://proxy.scalar.com; "
                + "worker-src 'self' blob:";

        http.securityMatcher("/scalar", "/scalar/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(scalarCsp))
                        // Scalar renders in-page; allow it to be framed same-origin if needed
                        .frameOptions(frame -> frame.sameOrigin())
                        .contentTypeOptions(Customizer.withDefaults())
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable())
                // If in the front-end you are using cookies for authentication, you should
                // enable CSRF protection
                // Disable CRSF if using local storage or session storage for authentication
                // tokens (e.g., JWT in Authorization header)
                // .csrf(csrf -> csrf
                // .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                // )
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // Allow async requests
                        .requestMatchers(
                                "/favicon.svg",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/logout",
                                "/api/v1/mfa/login",
                                "/error", // default error page by spring (must include)
                                "/ws/**", // websockets
                                "/actuator/**") // actuator for health check etc
                        .permitAll()
                        // This is to handle temporary token with temporary role
                        // This endpoint only accessibe by role MFA_CHECK
                        .requestMatchers("/api/v1/mfa/verify").hasAuthority("ROLE_MFA_CHECK")

                        // Ensure the user is authenticated AND does not possess ROLE_MFA_CHECK
                        .anyRequest().access((authentication, context) -> {
                            var authObj = authentication.get();

                            // Check if user is fully logged in (not anonymous)
                            boolean isAuthenticated = authObj != null && authObj.isAuthenticated()
                                    && !(authObj instanceof AnonymousAuthenticationToken);

                            // Check that they aren't stuck on the MFA step
                            boolean isNotMfaCheck = authObj != null
                                    ? authObj.getAuthorities().stream()
                                            .noneMatch(a -> a.getAuthority().equals("ROLE_MFA_CHECK"))
                                    : Boolean.FALSE;

                            return new AuthorizationDecision(isAuthenticated && isNotMfaCheck);
                        }))
                .exceptionHandling(exception -> exception
                        // Handles unauthenticated users trying to hit protected routes (Returns 401)
                        .authenticationEntryPoint((req, res, authException) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write(objectMapper.writeValueAsString(
                                    new ApiResponse<>(401, "Unauthorized",
                                            null)));
                        })
                        // Handles authenticated users who fail the .access() expression (Returns 403)
                        .accessDeniedHandler((req, res, accessDeniedException) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write(objectMapper.writeValueAsString(
                                    new ApiResponse<>(403, "Forbidden", null)));
                        }))
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/api/v1/oauth2", true))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Headers/Other filter chain configurations
        http.headers(headers -> headers
                // XSS (Cross-Site Scripting) Protection
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                // Clickjacking Protection (iFrame)
                .frameOptions(frame -> frame.deny())
                // MIME-Sniffing Protection
                .contentTypeOptions(Customizer.withDefaults())
                // Origin Must Use HTTPS (with duration of 1 year)
                // .httpStrictTransportSecurity(hsts ->
                // hsts.includeSubDomains(true).maxAgeInSeconds(31536000)
                // )
                // Referrer Policy
                .referrerPolicy(referrer -> referrer
                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)));

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
                "https://donnyk22.carrd.co", // Replace with your frontend app domain
                "http://localhost:8080"));
        // Allow specific HTTP methods
        config.setAllowedMethods(List.of(
                Action.GET.name(), Action.POST.name(), Action.PUT.name(), Action.PATCH.name(), Action.DELETE.name()));
        // Allow specific headers (e.g., Authorization for JWT tokens)
        config.setAllowedHeaders(List.of(
                "Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply CORS configuration to all endpoints
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
