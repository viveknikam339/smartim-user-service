package com.smartim.userservice.service.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisService redisService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    void get_withClass_shouldReturnObject_whenKeyExists() throws JsonProcessingException {
        // Given
        String key = "testKey";
        String json = "{\"field\":\"value\"}";
        TestObject expectedObject = new TestObject("value");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(json);
        when(objectMapper.readValue(json, TestObject.class)).thenReturn(expectedObject);

        // When
        TestObject result = redisService.get(key, TestObject.class);

        // Then
        assertNotNull(result);
        assertEquals(expectedObject.getField(), result.getField());
    }

    @Test
    void get_withClass_shouldReturnNull_whenKeyDoesNotExist() throws JsonProcessingException {
        // Given
        String key = "nonExistentKey";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        // When
        TestObject result = redisService.get(key, TestObject.class);

        // Then
        assertNull(result);
    }

    @Test
    void get_withTypeReference_shouldReturnObject_whenKeyExists() throws JsonProcessingException {
        // Given
        String key = "testKey";
        String json = "[{\"field\":\"value\"}]";
        List<TestObject> expectedList = Collections.singletonList(new TestObject("value"));
        TypeReference<List<TestObject>> typeReference = new TypeReference<>() {};

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(json);
        when(objectMapper.readValue(json, typeReference)).thenReturn(expectedList);

        // When
        List<TestObject> result = redisService.get(key, typeReference);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("value", result.get(0).getField());
    }

    @Test
    void set_shouldStoreObjectInRedis() throws JsonProcessingException {
        // Given
        String key = "testKey";
        TestObject obj = new TestObject("value");
        String json = "{\"field\":\"value\"}";
        long ttl = 3600L;

        when(objectMapper.writeValueAsString(obj)).thenReturn(json);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        redisService.set(key, obj, ttl);

        // Then
        verify(valueOperations).set(key, json, ttl, TimeUnit.SECONDS);
    }

    // Helper class for testing
    private static class TestObject {
        private String field;

        public TestObject(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }
}