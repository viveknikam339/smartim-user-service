package com.smartim.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> entityClass) throws JsonProcessingException {
        Object objString = redisTemplate.opsForValue().get(key);
        if (objString!=null){
            return objectMapper.readValue(objString.toString(), entityClass);
        }else{
            return null;
        }
    }

    public void set(String key, Object obj, Long timeToLive) throws JsonProcessingException {
        String jsonValue =  objectMapper.writeValueAsString(obj);
        redisTemplate.opsForValue().set(key, jsonValue, timeToLive, TimeUnit.SECONDS);
    }
}
