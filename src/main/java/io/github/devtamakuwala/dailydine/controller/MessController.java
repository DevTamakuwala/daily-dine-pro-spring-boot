package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.service.MessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all mess-related endpoints.
 * It provides functionality for administrators to manage mess accounts.
 */
@Slf4j
@RestController
@RequestMapping("/api/mess/")
// Enables Cross-Origin Resource Sharing (CORS) for all endpoints in this controller,
// allowing requests from any origin. This is useful for development with separate frontends.
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class MessController {

    @Autowired
    private MessService messService;

    /**
     * Retrieves a list of all unverified mess owners.
     * This endpoint is intended for administrators to review and approve new mess accounts.
     *
     * @param authentication The authentication principal.
     * @return A ResponseEntity containing a list of unverified mess owners, or an error message if the user is not authenticated.
     */
    @GetMapping("/")
    public ResponseEntity<?> getAllUnverifiedMess(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(messService.getAllUnverifiedMess(), HttpStatus.OK);
    }

}
