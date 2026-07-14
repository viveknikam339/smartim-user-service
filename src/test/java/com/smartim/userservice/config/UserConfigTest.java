package com.smartim.userservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartim.userservice.audit.AuditorAwareImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;

import static org.junit.jupiter.api.Assertions.*;

class UserConfigTest {

    private UserConfig userConfig;

    @BeforeEach
    void setUp() {
        userConfig = new UserConfig();
    }

    @Test
    void testObjectMapper() {
        ObjectMapper objectMapper = userConfig.objectMapper();

        assertNotNull(objectMapper);
        assertTrue(objectMapper.getRegisteredModuleIds().contains(new JavaTimeModule().getTypeId()));
        assertEquals(JsonInclude.Include.NON_NULL, objectMapper.getSerializationConfig().getSerializationInclusion());
        assertTrue(objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT));
        assertFalse(objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
    }

    @Test
    void testGetAuditorAwareImpl() {
        AuditorAware<String> auditorAware = userConfig.getAuditorAwareImpl();

        assertNotNull(auditorAware);
        assertTrue(auditorAware instanceof AuditorAwareImpl);
    }
}
