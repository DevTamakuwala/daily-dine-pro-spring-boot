package io.github.devtamakuwala.dailydine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * This configuration class is responsible for setting up Spring Data JPA's auditing feature.
 * The @EnableJpaAuditing annotation is the master switch that turns on the auditing functionality.
 */
@Configuration
// Enable JPA Auditing and tell it which bean to use for getting the auditor's ID.
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * This method creates and configures a bean of type AuditorAware.
     * This bean is now typed to return an Integer, matching our User entity's ID type.
     * Spring Data JPA will call this bean whenever it needs to know who the current user is.
     *
     * @return An instance of our custom AuditorAwareImpl, which provides the current user's ID as an Integer.
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        // This returns our custom implementation which gets the user ID from the security context.
        return new AuditorAwareImpl();
    }
}
