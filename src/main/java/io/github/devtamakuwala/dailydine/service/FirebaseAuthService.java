package io.github.devtamakuwala.dailydine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseAuthService {

    private static final String FIREBASE_AUTH_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyAtaSCmxOYOicA3znFpR-w5wrBUiMGF3xI";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String loginAndGetIdToken(String email, String password) {

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("email", email);
        requestPayload.put("password", password);
        requestPayload.put("returnSecureToken", true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestPayload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(FIREBASE_AUTH_URL, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("idToken");
            } else {
                throw new RuntimeException("Firebase login failed: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
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
}
