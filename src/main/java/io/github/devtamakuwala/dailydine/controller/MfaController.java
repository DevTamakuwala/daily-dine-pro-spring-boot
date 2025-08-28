package io.github.devtamakuwala.dailydine.controller;

import io.github.devtamakuwala.dailydine.model.User;
import io.github.devtamakuwala.dailydine.repository.UserRepository;
import io.github.devtamakuwala.dailydine.service.MfaService;
import io.github.devtamakuwala.dailydine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * A simple Data Transfer Object (DTO) for handling the verification request.
 * This class encapsulates the user's email and the verification code.
 */
class VerifyRequest {
    public String email;
    public String code;
}

/**
 * Controller for handling Multi-Factor Authentication (MFA) requests.
 * This controller provides endpoints for setting up and verifying MFA.
 */
@RestController
@RequestMapping("/api/mfa")
public class MfaController {

    @Autowired
    private MfaService mfaService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint to start the MFA setup process.
     * This method generates a new MFA secret and returns a QR code URI and a manual setup key.
     *
     * @param body A map containing the user's email.
     * @return A response entity containing the QR code URI and manual setup key, or an error message.
     */
    @PostMapping("/setup")
    public ResponseEntity<?> setupDevice(@RequestBody Map<String, String> body) {
        User user = userService.getUserByEmail(body.get("email"));
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (user.isMfaEnabled()) {
            return ResponseEntity.badRequest().body("MFA is already enabled for this account. Please disable it first if you wish to reset.");
        }

        final String secret = mfaService.generateNewSecret();
        user.setMfaSecret(secret);
        user.setMfaEnabled(false); // MFA is not yet active until verified
        userRepository.save(user);

        // This is where you provide both the QR code and the manual setup key
        return ResponseEntity.ok(Map.of(
                "qrCodeUri", mfaService.generateQrCodeImageUri(secret, user.getEmail()),
                "manualSetupKey", secret // This is the secret key for manual entry
        ));
    }

    /**
     * Endpoint to verify the Time-based One-Time Password (TOTP) and activate MFA.
     *
     * @param verifyRequest The request body containing the user's email and the verification code.
     * @return A response entity with a success or error message.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyRequest verifyRequest) {
        User user = userService.getUserByEmail(verifyRequest.email);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (mfaService.isOtpValid(user.getMfaSecret(), verifyRequest.code)) {
            user.setMfaEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("MFA has been enabled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Incorrect code. Please try again.");
        }
    }
}
