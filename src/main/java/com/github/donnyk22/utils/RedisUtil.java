package com.github.donnyk22.utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redis;

    @Value("${app.jwt.ttl-minutes}")
    private long TTL_MINUTES;

    @Value("${app.session.max}")
    private long MAX_SESSION_NUMBER;

    //uses same token will update the data
    public void store(String bucket, String identifier, String value, Integer ttl, TimeUnit unit) {
        redis.opsForValue().set(bucket + ":" + identifier, value, ttl, unit);
    }

    public String get(String bucket, String identifier) {
        return redis.opsForValue().get(bucket + ":" + identifier);
    }

    public void delete(String bucket, String identifier) {
        redis.delete(bucket + ":" + identifier);
    }

    public void updateKeepTTL(String bucket, String identifier, String newValue) {
        Long ttl = redis.getExpire(bucket + ":" + identifier, TimeUnit.SECONDS);

        if (ttl != null && ttl > 0) {
            redis.opsForValue().set(bucket + ":" + identifier, newValue, ttl, TimeUnit.SECONDS);
        }
    }

    public void storeToken(String token, String email, String sessionId) {
        String pattern = "session:" + email + ":*";
        Set<String> keys = redis.keys(pattern);
        
        if (keys != null && keys.size() >= MAX_SESSION_NUMBER) {
            // Delete the oldest session
            String oldestKey = keys.iterator().next();
            redis.delete(oldestKey);
        }

        String sessionKey = "session:" + email + ":" + sessionId;
        redis.opsForValue().set(sessionKey, token, TTL_MINUTES, TimeUnit.MINUTES);
    }

    public String getToken(String email, String sessionId) {
        return redis.opsForValue().get("session:" + email + ":" + sessionId);
    }

    public void deleteToken(String email, String sessionId) {
        redis.delete("session:" + email + ":" + sessionId);
    }

    public Boolean isTokenValid(String email, String sessionId) {
        return getToken(email, sessionId) != null;
    }
    
}
