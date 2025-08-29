package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.DTO.BackupCodeLoginDTO;
import io.github.devtamakuwala.dailydine.DTO.LoginDTO;
import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @Autowired
    private FirebaseAuthService firebaseAuthService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private MessService messService;
    @Autowired
    private MfaService mfaService;

    /**
     * Handles user login requests.
     *
     * @param login A LoginDTO object containing the user's email and encrypted password.
     * @return A ResponseEntity containing the Firebase ID token on successful authentication,
     * or an error message with an appropriate HTTP status code on failure.
     */
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDTO login) {
        // The password from the client is expected to be encrypted.
        // It is decrypted here before being sent to Firebase for verification.
        try {
            login.setPassword(DecryptionService.decryptPassword(login.getPassword()));
        } catch (Exception e) {
            log.error("Password decryption failed for user: {}", login.getEmail(), e);
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        ResponseEntity<String> response;
        try {
            // Attempt to log in using the Firebase service and get an ID token.
            String idToken = firebaseAuthService.loginAndGetIdToken(login.getEmail(), login.getPassword());
            // After successful authentication, retrieve the user's role from the database.
            String userRole = "";
            boolean visibile = false;
            if (idToken != null) {
                // Fetch the full user object to get role information.
                User user = userService.getUserByEmail(login.getEmail());
                userRole = user.getRole().name();
                visibile = user.isActive();
            }
            // Append the user's role to the ID token, separated by a space.
            // The frontend will need to parse this string to separate the token and the role.
            idToken += " " + userRole + " " + visibile;
            response = new ResponseEntity<>(idToken, HttpStatus.FOUND); // Use HttpStatus.OK for successful login.
        } catch (Exception e) {
            // If Firebase authentication fails, return the error message with an UNAUTHORIZED status.
            log.error("Firebase login failed for user: {}", login.getEmail(), e);
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    /**
     * Handles new user registration requests.
     * This endpoint creates a user in Firebase Authentication.
     *
     * @param user A User object containing the new user's details (email, password, etc.).
     * @return A ResponseEntity containing the new user's Firebase ID token on successful registration,
     * or an error message with an appropriate HTTP status code on failure.
     */
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // The password from the client is expected to be encrypted.
        // It is decrypted here before being sent to Firebase for user creation.
        try {
//            if (!user.getPassword().equals("jenil@1234")) {
            user.setPassword(DecryptionService.decryptPassword(user.getPassword()));
//            }
        } catch (Exception e) {
            log.error("Password decryption failed during registration for user: {}", user.getEmail(), e);
            return new ResponseEntity<>("Invalid registration data", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<String> response;
        try {
            // Before saving the user, ensure that the bidirectional relationship is correctly set.
            // If a customer is present, set the user on the customer to maintain consistency.
            if (user.getCustomer() != null) {
                user.getCustomer().setUserId(user);
            }
            // Similarly, if a mess is present, set the user on the mess.
            if (user.getMess() != null) {
                user.getMess().setUserId(user);
            }
            // With CascadeType.ALL configured on the User entity, calling createUser will now automatically
            // save the associated Customer and Mess entities. The explicit calls to customerService.createCustomer
            // and messService.createMess have been removed to simplify the code and rely on cascading persistence.
            userService.createUser(user);
            String idToken = firebaseAuthService.registerUser(user.getEmail(), user.getPassword());
            response = new ResponseEntity<>(idToken, HttpStatus.CREATED);
        } catch (Exception e) {
            // If Firebase registration fails (e.g., email already exists), return the error message.
            log.error("Firebase registration failed for user: {}", user.getEmail(), e);
            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Handles user login with a backup code.
     * This endpoint allows users to log in with a backup code if they don't have access to their authenticator app.
     *
     * @param login A BackupCodeLoginDTO object containing the user's email, password, and backup code.
     * @return A ResponseEntity containing the Firebase ID token, user role, and visibility on successful authentication,
     * or an error message with an appropriate HTTP status code on failure.
     */
    @PostMapping("login-backup")
    public ResponseEntity<Map<String, Object>> loginWithBackupCode(@RequestBody BackupCodeLoginDTO login) {
        Map<String, Object> response = new HashMap<>();
        try {
            // First, verify the user's password
            login.setPassword(DecryptionService.decryptPassword(login.getPassword()));
            String idToken = firebaseAuthService.loginAndGetIdToken(login.getEmail(), login.getPassword());
            if (idToken == null) {
                response.put("error", "Invalid credentials");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            User user = userService.getUserByEmail(login.getEmail());

            if (!user.isMfaEnabled()){
                response.put("Error", "MFA is not verified");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


            if (mfaService.verifyBackupCode(user, login.getBackupCode())) {
                String role = user.getRole().name();
                boolean visible = user.isActive();
                response.put("idToken", idToken);
                response.put("role", role);
                response.put("visible", visible);
                userService.createUser(user); // This will save the updated user
                return new ResponseEntity<>(response, HttpStatus.FOUND);
            } else {
                response.put("Error", "Invalid backup code.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            response.put("Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

}
