package io.github.devtamakuwala.dailydine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * The main entry point for the DailyDine Spring Boot application.
 * Note: The @EnableJpaAuditing annotation was intentionally removed from this file
 * and moved to JpaConfig.java to resolve a bean definition conflict and to centralize
 * persistence-related configurations.
 */
@SpringBootApplication
@EnableCaching
public class DailyDineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyDineApplication.class, args);
    }

}
