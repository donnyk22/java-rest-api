package com.github.donnyk22.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenUtil {

    private final StringRedisTemplate redis;

    @Value("${app.jwt.ttl-minutes}")
    private long TTL_MINUTES;

    public RedisTokenUtil(StringRedisTemplate redis) {
        this.redis = redis;
    }

    //uses same token will update the data
    public void store(String bucket, String token, String value, Integer ttl, TimeUnit unit) {
        redis.opsForValue().set(bucket + ":" + token, value, ttl, unit);
    }

    public String get(String bucket, String token) {
        return redis.opsForValue().get(bucket + ":" + token);
    }

    public void updateKeepTTL(String bucket, String token, String newValue) {
        Long ttl = redis.getExpire(bucket + ":" + token, TimeUnit.SECONDS);

        if (ttl != null && ttl > 0) {
            redis.opsForValue().set(bucket + ":" + token, newValue, ttl, TimeUnit.SECONDS);
        }
    }

    public void delete(String bucket, String token) {
        redis.delete(bucket + ":" + token);
    }

    public void storeToken(String token, String email) {
        redis.opsForValue().set("token:" + token, email, TTL_MINUTES, TimeUnit.MINUTES);
        redis.opsForValue().set("user:" + email, token, TTL_MINUTES, TimeUnit.MINUTES);
    }

    public Boolean isTokenValid(String token) {
        return redis.opsForValue().get("token:" + token) != null;
    }

    public String getTokenByEmail(String email) {
        return redis.opsForValue().get("user:" + email);
    }

    public void deleteToken(String token, String email) {
        redis.delete("token:" + token);
        redis.delete("user:" + email);
    }

    public void deleteTokenByEmail(String email) {
        String token = getTokenByEmail(email);
        if (token != null) {
            deleteToken(token, email);
        }
    }
    
}
