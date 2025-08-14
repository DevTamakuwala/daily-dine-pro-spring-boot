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
// The generic type is set to <Integer> to match the data type of the User entity's primary key.
public class AuditorAwareImpl implements AuditorAware<Integer> {

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

        // If there is no authenticated user (e.g., for public endpoints), we cannot determine the auditor.
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // The user's principal identifier is retrieved from the Authentication object.
        // In Spring Security, authentication.getName() typically returns a String.
        String principalName = authentication.getName();

        try {
            // Since our user IDs are Integers, we must parse the String identifier.
            // This successfully converts the user ID into the required Integer format.
            return Optional.of(Integer.parseInt(principalName));
        } catch (NumberFormatException e) {
            // This is a crucial safeguard. If the principal's name is not a valid integer
            // (e.g., for system processes or anonymous users), we log a warning and return an empty Optional
            // to prevent application crashes and indicate that no user auditor is available.
            log.warn("Could not parse user ID from principal. Found: {}. Returning empty auditor.", principalName);
            return Optional.empty();
        }
    }
}
