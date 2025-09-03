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
 * This class customizes the default behavior of Jackson to ensure consistent and correct
 * serialization of Java objects to JSON, particularly for date and time types.
 */
@Configuration
public class JacksonConfig {

    /**
     * Creates a primary ObjectMapper bean to be used throughout the application.
     * This configuration ensures that Java 8 Date and Time APIs (like Instant)
     * are serialized to a human-readable string format instead of a numeric timestamp.
     *
     * @param builder The Jackson2ObjectMapperBuilder used to construct the ObjectMapper.
     * @return A configured ObjectMapper instance.
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
