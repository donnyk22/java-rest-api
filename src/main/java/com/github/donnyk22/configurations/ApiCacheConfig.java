package com.github.donnyk22.configurations;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class ApiCacheConfig {

    @Value("${app.cache.ttl-minutes}")
    private Integer EXPIRATION;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisSerializer<Object> serializer = RedisSerializer.json();

        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(EXPIRATION))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            );
    }
}