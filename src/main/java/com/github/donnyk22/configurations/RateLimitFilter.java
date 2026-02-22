package com.github.donnyk22.configurations;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.donnyk22.models.dtos.ApiResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${app.ratelimit.max-req}")
    private Integer MAX_REQUESTS;

    @Value("${app.ratelimit.max-req-minutes}")
    private Integer MAX_REQ_DURATION;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::createBucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            sendTooManyReqResponse(response, "Too Many Requests");
        }
    }

    private Bucket createBucket(String key) {
    Bandwidth limit = Bandwidth.builder()
        .capacity(MAX_REQUESTS)
        .refillIntervally(MAX_REQUESTS, Duration.ofMinutes(MAX_REQ_DURATION))
        .build();

        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    private void sendTooManyReqResponse(HttpServletResponse res, String errorMessage) throws IOException {
        res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        res.setContentType("application/json");
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            errorMessage,
            null
        );
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(response);
        res.getWriter().write(json);
    }

    // Uncomment the following method to apply rate limiting only to specific URL patterns
    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) {
    //     return !request.getRequestURI().startsWith("/api");
    // }

}
