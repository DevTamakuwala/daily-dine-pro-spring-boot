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
// @EnableJpaAuditing is placed here to activate the auditing feature for the entire application.
// It was removed from the main application class to prevent bean definition conflicts and keep configuration clean.
// The 'auditorAwareRef' attribute points to the bean that will provide the user IDs.
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * This method creates and configures the AuditorAware bean.
     * The bean is now correctly typed to AuditorAware<Integer> to align with the Integer user ID type.
     * Spring Data JPA calls this bean to get the current user's ID for populating the createdBy and modifiedBy fields.
     *
     * @return An instance of our custom AuditorAwareImpl, which provides the current user's ID as an Integer.
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        // This returns our custom implementation that gets the user ID from the security context.
        return new AuditorAwareImpl();
    }
}
