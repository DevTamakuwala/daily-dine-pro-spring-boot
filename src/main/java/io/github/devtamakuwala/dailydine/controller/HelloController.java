package io.github.devtamakuwala.dailydine.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is used for testing and demonstrating the security configuration.
 * It provides both public and private endpoints to verify that authentication and authorization are working as expected.
 */
@RestController
@RequestMapping("/api")
public class HelloController {

    /**
     * A public endpoint that can be accessed by anyone without authentication.
     * Useful for checking if the application is running and accessible.
     *
     * @return A simple greeting message.
     */
    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello from a public endpoint! You don't need to be authenticated.";
    }

    /**
     * A private endpoint that requires a valid authentication token to be accessed.
     * Useful for testing if the token validation and user identification are working correctly.
     *
     * @param authentication The Authentication object, automatically populated by Spring Security from the user's token.
     * @return A personalized greeting message including the user's unique ID (UID).
     */
    @GetMapping("/private/hello")
    public String privateHello(Authentication authentication) {
        // Spring Security populates the Authentication object upon successful token validation.
        // The 'authentication.getName()' method returns the principal's identifier, which is the user's UID from the Firebase token.
        String userId = authentication.getName();
        return "Hello, " + userId + "! You are accessing a private endpoint.";
    }
}
