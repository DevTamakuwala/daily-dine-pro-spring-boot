package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.DTO.VerifyRequestDTO;
import io.github.devtamakuwala.dailydine.service.MfaService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * A simple Data Transfer Object (DTO) for handling the verification request.
 * This class encapsulates the user's email and the verification code.
 * <p>
 * Controller for handling Multi-Factor Authentication (MFA) requests.
 * This controller provides endpoints for setting up and verifying MFA.
 */
@RestController
@RequestMapping("/api/mfa")
public class MfaController {
    /**
     * Endpoint to start the MFA setup process.
     * This method generates a new MFA secret and returns a QR code URI and a manual setup key.
     */
    private final MfaService mfaService;


    public MfaController(MfaService mfaService) {
        this.mfaService = mfaService;
    }


    /**
     * Set up MFA
     *
     */
    @PostMapping("/setup")
    public ResponseEntity<?> setupDevice(@RequestBody Map<String, String> body) {
        return mfaService.setUpMfa(body);
    }

    /**
     * Verify MFA
     *
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyRequestDTO verifyRequest) {
        return mfaService.verifyCode(verifyRequest);
    }

    /**
     * Delete MFA from user
     */
    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<?> removeMfa(@RequestBody VerifyRequestDTO verifyRequest) {
        return mfaService.deleteMfa(verifyRequest);
    }
}
