package com.smartim.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuration class for setting up Redis integration.
 * Defines a RedisTemplate bean for performing Redis operations with String keys and values.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a RedisTemplate bean with String serialization for both keys and values.
     *
     * @param factory the Redis connection factory (auto-configured by Spring)
     * @return configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
