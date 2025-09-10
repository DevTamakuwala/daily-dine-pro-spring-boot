package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.DTO.BackupCodeLoginDTO;
import io.github.devtamakuwala.dailydine.DTO.LoginDTO;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This controller handles all authentication-related endpoints, such as user login and registration.
 * It acts as the primary entry point for users to gain access to the application.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth/")
// Enables Cross-Origin Resource Sharing (CORS) for all endpoints in this controller,
// allowing requests from any origin. This is useful for development with separate frontends.
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Login for user
     *
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        return authService.login(login);
    }


    /**
     * Register User
     *
     */
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return authService.register(user);
    }


    /**
     * Login with back up code
     *
     */
    @PostMapping("login-backup")
    public ResponseEntity<Map<String, Object>> loginWithBackupCode(@RequestBody BackupCodeLoginDTO login) {
        return authService.loginWithBackUpCodes(login);
    }

}
