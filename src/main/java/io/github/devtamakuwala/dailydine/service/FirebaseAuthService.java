package io.github.devtamakuwala.dailydine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This service handles direct communication with the Firebase Authentication REST API.
 * It is responsible for sending login and registration requests to Firebase and returning the resulting ID token.
 */
@Service
public class FirebaseAuthService {

    static String FIREBASE_API_KEY = "AIzaSyAtaSCmxOYOicA3znFpR-w5wrBUiMGF3xI";
    // The Firebase REST API endpoint for signing in a user with an email and password.
    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + FIREBASE_API_KEY;
    // The Firebase REST API endpoint for creating a new user account.
    private static final String FIREBASE_SIGNUP_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + FIREBASE_API_KEY;
    // The Firebase REST API endpoint for changing a user's password.
    private static final String FIREBASE_RESET_URL = "https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Authenticates a user with Firebase using their email and password.
     */
    public String loginAndGetIdToken(String email, String password) {
        // Create the request payload for the Firebase API.
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("email", email);
        requestPayload.put("password", password);
        requestPayload.put("returnSecureToken", true); // This is required to get the ID token.

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

        try {
            // Send the POST request to the Firebase login endpoint.
            ResponseEntity<Map> response = restTemplate.postForEntity(FIREBASE_AUTH_URL, request, Map.class);

            // On success, extract and return the ID token from the response body.
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("idToken");
            } else {
                throw new RuntimeException("Firebase login failed with status: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            // If Firebase returns an error (e.g., wrong password), parse the specific error message.
            try {
                Map<String, Object> errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, Object> error = (Map<String, Object>) errorResponse.get("error");
                String message = (String) error.get("message");
                throw new RuntimeException(message, e);
            } catch (IOException ex) {
                // This fallback is for cases where the error response itself is malformed.
                throw new RuntimeException("Error parsing Firebase error response", ex);
            }
        }
    }

    /**
     * Registers a new user with Firebase using an email and password.
     */
    public String registerUser(String email, String password) {
        // Create the request payload for the Firebase API.
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("email", email);
        requestPayload.put("password", password);
        requestPayload.put("returnSecureToken", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

        try {
            // Send the POST request to the Firebase registration endpoint.
            ResponseEntity<Map> response = restTemplate.postForEntity(FIREBASE_SIGNUP_URL, request, Map.class);

            // On success, extract and return the ID token.
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("idToken");
            } else {
                throw new RuntimeException("Firebase registration failed with status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            // If Firebase returns an error (e.g., email already exists), parse the specific error message.
            try {
                Map<String, Object> errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                Map<String, Object> error = (Map<String, Object>) errorResponse.get("error");
                String message = (String) error.get("message");
                throw new RuntimeException(message, e);
            } catch (IOException ex) {
                throw new RuntimeException("Error parsing Firebase error response", ex);
            }
        }
    }

    public ResponseEntity<?> sendPasswordResetEmail(String email) {
        RestTemplate restTemplate = new RestTemplate();

        String url = FIREBASE_RESET_URL + FIREBASE_API_KEY;

        Map<String, Object> body = new HashMap<>();
        body.put("requestType", "PASSWORD_RESET");
        body.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
