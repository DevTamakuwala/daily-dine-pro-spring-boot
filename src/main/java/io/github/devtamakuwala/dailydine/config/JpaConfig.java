package io.github.devtamakuwala.dailydine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * This is the central configuration class for Spring Data JPA features.
 * It is the single, authoritative source for enabling and configuring JPA auditing.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * This method creates and configures the AuditorAware bean.
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        // This returns our custom implementation that gets the user ID from the security context.
        return new AuditorAwareImpl();
    }
}
