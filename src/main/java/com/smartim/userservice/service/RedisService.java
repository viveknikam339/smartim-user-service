package com.smartim.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.concurrent.TimeUnit;

/**
 * Service class for interacting with Redis to cache and retrieve objects.
 * This class provides generic methods to serialize Java objects into JSON
 * and store them in Redis, as well as deserialize JSON values from Redis
 * back into Java objects using Jackson's {@link ObjectMapper}.
 * Features:
 *   Generic get method to retrieve and deserialize cached data
 *   Generic set method to serialize and store data with a TTL
 * Typical usage:
 * @code
 * UserDto user = redisService.get("user_email_test@example.com", UserDto.class);
 * List<UserDto> users = redisService.get("ADMIN", new TypeReference<List<UserDto>>() {});
 * redisService.set("user_email_test@example.com", user, 3600L);
 *
 */
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Retrieves a cached object from Redis and deserializes it into the specified class.
     *
     * @param key the Redis key where the object is stored.
     * @param entityClass the class to which the JSON should be deserialized.
     * @param <T> the type of the object to return.
     * @return deserialized object from Redis, or {@code null} if not found.
     * @throws JsonProcessingException if deserialization fails.
     */
    public <T> T get(String key, Class<T> entityClass) throws JsonProcessingException {
        Object objString = redisTemplate.opsForValue().get(key);
        if (objString!=null){
            return objectMapper.readValue(objString.toString(), entityClass);
        }else{
            return null;
        }
    }

    /**
     * Retrieves a cached JSON value from Redis and deserializes it into the specified generic type
     * using a {@link TypeReference}.
     * This method supports deserialization of collections or complex types such as
     * {@code List<UserDto>} or {@code Map<String, Object>}.
     *
     * @param key           the Redis key where the object is stored.
     * @param typeReference the type reference indicating the desired return type.
     * @param <T>           the type of the object to return.
     * @return deserialized object from Redis, or {@code null} if not found.
     * @throws JsonProcessingException if deserialization fails.
     */
    public <T> T get(String key, TypeReference<T> typeReference) throws JsonProcessingException {
        Object objString = redisTemplate.opsForValue().get(key);
        if (objString!=null){
            return objectMapper.readValue(objString.toString(), typeReference);
        }else{
            return null;
        }
    }

    /**
     * Serializes the given object into JSON and stores it in Redis with a time-to-live (TTL).
     *
     * @param key the Redis key under which the object is stored.
     * @param obj the object to cache.
     * @param timeToLive TTL for the object in seconds.
     * @throws JsonProcessingException if serialization fails.
     */
    public void set(String key, Object obj, Long timeToLive) throws JsonProcessingException {
        String jsonValue =  objectMapper.writeValueAsString(obj);
        redisTemplate.opsForValue().set(key, jsonValue, timeToLive, TimeUnit.SECONDS);
    }
}
