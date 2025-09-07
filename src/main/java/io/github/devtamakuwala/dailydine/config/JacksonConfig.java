package io.github.devtamakuwala.dailydine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration class for Jackson, the JSON processing library used by Spring Boot.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a primary ObjectMapper bean to be used throughout the application.
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        // Register the JavaTimeModule to handle Java 8 date and time types.
        objectMapper.registerModule(new JavaTimeModule());
        // Configure Jackson to write dates as ISO-8601 strings instead of timestamps.
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
