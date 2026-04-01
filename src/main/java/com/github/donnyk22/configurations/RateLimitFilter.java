package com.github.donnyk22.configurations;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    private Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Value("${app.ratelimit.max-req}")
    private Integer maxRequests;

    @Value("${app.ratelimit.max-req-minutes}")
    private Integer maxReqDuration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, this::createBucket);

        if (bucket.tryConsume(1)) { // consume 1 means one request count as 1. use 5 or 10 if the request is heavy
                                    // (need to apply to specific endpoint with heavy request)
            filterChain.doFilter(request, response);
        } else {
            sendTooManyReqResponse(response, "Too Many Requests");
        }
    }

    private Bucket createBucket(String key) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(maxRequests)
                .refillIntervally(maxRequests, Duration.ofMinutes(maxReqDuration))
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
                null);
        String json = objectMapper.writeValueAsString(response);
        res.getWriter().write(json);
    }

    // Uncomment the following method to apply rate limiting only to specific URL
    // patterns
    // @Override
    // protected boolean shouldNotFilter(HttpServletRequest request) {
    // return !request.getRequestURI().startsWith("/api");
    // }

}
