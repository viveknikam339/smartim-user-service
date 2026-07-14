package com.smartim.userservice.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RedisConfigTest {

    private RedisConfig redisConfig;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @BeforeEach
    void setUp() {
        redisConfig = new RedisConfig();
    }

    @Test
    void testRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = redisConfig.redisTemplate(redisConnectionFactory);

        assertNotNull(redisTemplate);
        assertEquals(redisConnectionFactory, redisTemplate.getConnectionFactory());
        assertEquals(StringRedisSerializer.class, redisTemplate.getKeySerializer().getClass());
        assertEquals(StringRedisSerializer.class, redisTemplate.getValueSerializer().getClass());
    }
}
