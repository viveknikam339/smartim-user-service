package com.smartim.userservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartim.userservice.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class UserConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper =new ObjectMapper();
        //Register Java 8 time module
        mapper.registerModule(new JavaTimeModule());
        //Include only non-null values
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Provide pretty JSON output
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //Write dates as timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    AuditorAware<String> getAuditorAwareImpl(){
        return new AuditorAwareImpl();
    }
}
