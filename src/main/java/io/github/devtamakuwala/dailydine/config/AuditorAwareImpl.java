package io.github.devtamakuwala.dailydine.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * This class implements the AuditorAware interface from Spring Data JPA.
 * Its purpose is to provide the application with the identity of the current user,
 * so that the createdBy and modifiedBy fields in our auditable entities can be populated automatically.
 * This implementation is specifically configured to handle Integer user IDs.
 */
public class AuditorAwareImpl implements AuditorAware<Integer> { // Changed to <Integer> to match the User ID type.

    private static final Logger log = LoggerFactory.getLogger(AuditorAwareImpl.class);

    /**
     * This is the core method of the AuditorAware interface.
     * Spring Data JPA will call this method whenever it needs to know who the current user is.
     *
     * @return An Optional containing the current user's ID as an Integer.
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // Spring Security stores information about the currently authenticated user in the SecurityContextHolder.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // If there is no authenticated user, we cannot determine the auditor.
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // The user's principal identifier is retrieved from the Authentication object.
        // This is typically a String, so we need to convert it to an Integer.
        String principalName = authentication.getName();

        try {
            // We attempt to parse the String identifier into an Integer.
            return Optional.of(Integer.parseInt(principalName));
        } catch (NumberFormatException e) {
            // This catch block is a safeguard. It handles cases where the principal's name
            // is not a valid integer. This might happen with anonymous users or system processes.
            log.warn("Could not parse user ID from principal. Found: {}. Returning empty auditor.", principalName);
            return Optional.empty();
        }
    }
}
